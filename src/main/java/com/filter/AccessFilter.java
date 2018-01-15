package com.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter{

	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
	
	/**过滤器的逻辑代码*/
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		
		Object accessToken = request.getParameter("accessToken");
		if(null == accessToken){
			log.warn("access token is empty");
			//令zuul过滤该请求，不对其进行路由
			ctx.setSendZuulResponse(false);
			//设置返回的错误码
			ctx.setResponseStatusCode(401);
			//设置返回的页面body
			ctx.setResponseBody("<h1>Error 401 : access token is empty!</h1>");
			return null;
		}
		log.info("access token ok");
		return null;
	}

	/**
	 * 返回一个boolean类型来判断该过滤器是否要执行，
	 * 通过此函数可实现过滤器的开关。
	 * @return true:改过滤器总是生效
	 * */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**通过Int值来定义过滤器的执行顺序*/
	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 * 返回过滤器类型
	 * @return 
	 * @pre 可以在请求被路由之前调用
	 * @routing 在路由请求时候被调用
	 * @post 在routing和error过滤器之后被调用
	 * @error 处理请求时发生错误时被调用
	 * */
	@Override
	public String filterType() {
		return "pre";
	}

}
