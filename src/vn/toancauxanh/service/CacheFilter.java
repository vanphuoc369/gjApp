package vn.toancauxanh.service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public final class CacheFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		int hour = 24*30;
		res.setDateHeader("Expires", System.currentTimeMillis() + hour*60*60*1000); 
		res.addHeader("Cache-Control", "max-age="+hour*60*60);
		//if ((validEncodings != null) && (validEncodings.indexOf("gzip") > -1)) {
        //    CompressionResponseWrapper wrappedResponse = new CompressionResponseWrapper(res);
        //    filterChain.doFilter(request, wrappedResponse);
        //    wrappedResponse.finish();
        //}
        //else {
        	filterChain.doFilter(request, response);
        //}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) {
		// TODO Auto-generated method stub

	}
}
