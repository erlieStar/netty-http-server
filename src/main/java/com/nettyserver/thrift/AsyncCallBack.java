package com.nettyserver.thrift;

import com.nettyserver.bean.RequestBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaders.Values;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.transport.TNonblockingTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public abstract class AsyncCallBack<T> implements AsyncMethodCallback<T> {
	protected Logger log = LoggerFactory.getLogger(getClass());
	private RequestBean bean;
	private TNonblockingTransport transport;
	private static final byte[] CONTENT = { 'E', 'X', 'C', 'U', 'T', 'E', ' ', 'E', 'R', 'R', 'O', 'R' };

	public abstract String getResult(T response) throws TException;

	
	public AsyncCallBack() {
	}
	
	
	public AsyncCallBack(RequestBean bean, TNonblockingTransport transport) {
		this.setBean(bean);
		this.transport = transport;
	}

	public void processOver(RequestBean reqBean, String responseStr) throws Exception {
		boolean keepAlive = HttpHeaders.isKeepAlive(reqBean.getReq());
		byte[] responseByte = CONTENT;
		HttpResponseStatus status = OK;
		responseByte = responseStr.getBytes("UTF-8");
		ByteBuf responseBuf = Unpooled.wrappedBuffer(responseByte);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, responseBuf);
		response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
		// System.out.println(response.content().readableBytes());
		response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
		if (!keepAlive) {
			reqBean.getCtx().write(response).addListener(ChannelFutureListener.CLOSE);
			reqBean.getCtx().writeAndFlush(response);
		} else {
			response.headers().set(CONNECTION, Values.KEEP_ALIVE);
			reqBean.getCtx().writeAndFlush(response);
		}
		if (reqBean.getReq() instanceof FullHttpRequest && reqBean.getReq().getDecoderResult().isSuccess()) {
			// ((FullHttpRequest) req).release();
			reqBean.setReq(null);
		} else {
			log.warn("invalud request type: {}", reqBean.getReq().getClass());
		}
		// excute end write logs
		long endTime = System.currentTimeMillis();
		reqBean.addItemLog("allUse:" + (endTime - reqBean.getReadTime()) + " ms");
		log.info("AsyncCallBack thread-{} {}",reqBean.getThreadId(),reqBean.getLogObj());
		// reqBean.getCtx().channel().close();
		reqBean = null;
	}

	private void builtResult(String resStr) {
		if (resStr.equals("") || null == resStr) {
			getBean().setResState(ResultState.EMPDATA);
		}
		String resultStr = "";
		long time1 = System.currentTimeMillis();
		try {
			resultStr = HttpExcecuteService.buildResultString(getBean().getMethod(), resStr, getBean().getResState(),
					getBean().getExcuteTime(),"");
		} catch (Exception e1) {
			log.error("aysn method buildResult error",e1);
		}
		long time2 = System.currentTimeMillis();
		if (CdapiServer.isOnline) {
			CommUtil.getPool2().submit(new StatisThread(getBean().getRemoteIp(), getBean().getMethod(),
					getBean().getResState(), getBean().getAppid(), getBean().getExcuteTime()));
		}
		long eetime = System.currentTimeMillis();
		getBean().addItemLog("toStringUse:" + (time2 - time1) + " ms");
		getBean().addItemLog("processUse:" + (eetime - getBean().getExcuteTime()) + " ms");
		getBean().addItemLog("state:" + getBean().getResState().name());
		getBean().addItemLog("isAsyn:" + getBean().isAsyncFlag());
		try {
			processOver(getBean(), resultStr);
		} catch (Exception e) {
			log.error("aysn method excute response error",e);
		}
	}

	@Override
	public void onComplete(T response) {
		String resStr = "";
		try {
			resStr = getResult(response);
			log.debug("onComplete getResult: {}", resStr);
		} catch (TException e) {
			getBean().setResState(ResultState.EXEERROR);
			log.error("aysn method excute error",e);
		} finally {
			if (transport != null)
				transport.close();
		}
		// Netty response
		builtResult(resStr);
	}

	// 返回异常
	@Override
	public void onError(Exception exception) {
		try {
			log.error("AsyncCallBack onError,request failed,method {}", bean.getMethod(), exception);
			getBean().setResState(ResultState.EXEERROR);
			builtResult(ResultState.EXEERROR.getName());
		} finally {
			if (transport != null)
				transport.close();
		}
	}

	public RequestBean getBean() {
		return bean;
	}

	public void setBean(RequestBean bean) {
		this.bean = bean;
	}


	public TNonblockingTransport getTransport() {
		return transport;
	}


	public void setTransport(TNonblockingTransport transport) {
		this.transport = transport;
	}

	
}
