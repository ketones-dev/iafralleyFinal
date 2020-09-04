package com.cdac.iafralley.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

public class SameSiteFilter extends GenericFilterBean {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		Cookie[] allCookies = req.getCookies();

		if (allCookies != null) {
			Cookie session = Arrays.stream(allCookies).filter(x -> x.getName().equals("iaf_rally")).findFirst()
					.orElse(null);

			if (session != null) {
				session.setHttpOnly(true);
				
				session.setSecure(true);
				session.setPath("/");
				resp.addCookie(session);
				addSameSiteCookieAttribute((HttpServletResponse) resp); // add SameSite=strict cookie attribute
			}

		}

		chain.doFilter(request, response);

	}

	private void addSameSiteCookieAttribute(HttpServletResponse response) {
		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		boolean firstHeader = true;
		for (String header : headers) { // there can be multiple Set-Cookie attributes
			if (firstHeader) {
				response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Lax"));
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; %s", header, "SameSite=Lax"));
		}
	}
}
