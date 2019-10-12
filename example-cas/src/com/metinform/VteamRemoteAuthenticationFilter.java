package com.metinform;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;


import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VteamRemoteAuthenticationFilter extends AbstractCasFilter{
	protected static Logger log = LoggerFactory.getLogger(VteamRemoteAuthenticationFilter.class);
	public static final String CONST_CAS_GATEWAY = "_const_cas_gateway_";
	private String localLoginUrl;
	private String casServerLoginUrl;
	private boolean renew = false;
	private boolean gateway = false;
	private String ignorePattern;
	
	protected void initInternal(final FilterConfig filterConfig)throws ServletException{
		super.initInternal(filterConfig);
		setCasServerLoginUrl(getPropertyFromInitParams(filterConfig,"casServerLoginUrl", null));
		log.trace("Loaded CasServerLoginUrl parameter: "+ this.casServerLoginUrl);
		setLocalLoginUrl(getPropertyFromInitParams(filterConfig,"localLoginUrl", null));
		log.trace("Loaded LocalLoginUrl parameter: " + this.localLoginUrl);
		setRenew(Boolean.parseBoolean(getPropertyFromInitParams(filterConfig,"renew", "false")));
		log.trace("Loaded renew parameter: " + this.renew);
		setGateway(Boolean.parseBoolean(getPropertyFromInitParams(filterConfig,"gateway", "false")));
		log.trace("Loaded gateway parameter: " + this.gateway);
        setIgnorePattern(getPropertyFromInitParams(filterConfig, "ignorePattern", null));
        log.trace("Loaded ignorePattern parameter: " + this.ignorePattern);
	}
	
	public void init()
	{
		super.init();
		CommonUtils.assertNotNull(this.localLoginUrl,"localLoginUrl cannot be null.");
		CommonUtils.assertNotNull(this.casServerLoginUrl,"casServerLoginUrl cannot be null.");
	}
	
	private boolean isExclude(String uri){
        boolean isInWhiteList = false;
        if(ignorePattern!=null && uri!=null){
        	String[] _excludePath = ignorePattern.trim().split(",");
        	for (String string : _excludePath) {
        		isInWhiteList = uri.matches(string);
        		if(isInWhiteList)
        			return isInWhiteList;
			}
        }
        return isInWhiteList;  
    }  
	
	public final void doFilter(final ServletRequest servletRequest,final ServletResponse servletResponse, final FilterChain filterChain)throws IOException, ServletException
	{
		
System.out.println("$$$$do filter .........................");		

		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final HttpSession session = request.getSession(false);
		final String ticket = request.getParameter(getArtifactParameterName());
		final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;
		
		if (null != session) {
			System.out.println("111111111:" + session.getAttribute(CONST_CAS_ASSERTION).getClass().getClassLoader().toString());
		}
		
		System.out.println("22222222222: " + Assertion.class.getClassLoader().toString());

System.out.println("$$$$getServletPath:" + request.getServletPath());

System.out.println("$$$$getRequestURL"+ request.getRequestURL().toString());
		
		
//		if(isExclude(request.getServletPath())){  
//            filterChain.doFilter(request, response);  
//System.out.println("$$$$is exclude == true");            
//            return;  
//        }  


		String requestUrl = request.getRequestURL().toString();
		
		
		if (requestUrl.indexOf("SsoServlet") > -1) {
		
			final boolean wasGatewayed = session != null && session.getAttribute(CONST_CAS_GATEWAY) != null;
			URL url = new URL(localLoginUrl);
			final boolean isValidatedLocalLoginUrl = request.getRequestURI().endsWith(url.getPath())
					&& CommonUtils.isNotBlank(request.getParameter("validated"));
			
			if (!isValidatedLocalLoginUrl && CommonUtils.isBlank(ticket) && assertion == null && !wasGatewayed)
			{
				log.debug("no ticket and no assertion found");
				if (this.gateway)
				{
					log.debug("setting gateway attribute in session");
					request.getSession(true).setAttribute(CONST_CAS_GATEWAY, "yes");
				}
				final String serviceUrl = constructServiceUrl(request, response);
				if (log.isDebugEnabled())
				{
					log.debug("Constructed service url: " + serviceUrl);
				}
				String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(),serviceUrl, this.renew, this.gateway);
				urlToRedirectTo += (urlToRedirectTo.contains("?") ? "&" : "?") + "loginUrl=" + URLEncoder.encode(localLoginUrl, "utf-8");
				log.debug("redirecting to \"" + urlToRedirectTo + "\"");
				response.sendRedirect(urlToRedirectTo);
				return;
			}
			if (session != null)
			{
				log.debug("removing gateway attribute from session");
				session.setAttribute(CONST_CAS_GATEWAY, null);
			}
			filterChain.doFilter(request, response);
			
	System.out.println("$$$$done...................");		

		} else {
			filterChain.doFilter(request, response);
		}
	}
	
	public final void setRenew(final boolean renew)
	{
		this.renew = renew;
	}
	
	public final void setGateway(final boolean gateway)
	{
		this.gateway = gateway;
	}
	
	public final void setCasServerLoginUrl(final String casServerLoginUrl)
	{
		this.casServerLoginUrl = casServerLoginUrl;
	}
	
	public final void setLocalLoginUrl(String localLoginUrl)
	{
		this.localLoginUrl = localLoginUrl;
	}

	public String getIgnorePattern() {
		return ignorePattern;
	}

	public void setIgnorePattern(String ignorePattern) {
		this.ignorePattern = ignorePattern;
	}
	
}


