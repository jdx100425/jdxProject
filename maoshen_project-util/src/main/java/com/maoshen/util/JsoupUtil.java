package com.maoshen.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class JsoupUtil {
	private static Logger log = LoggerFactory.getLogger(JsoupUtil.class);

	
	public static String Html2TextTitle(String htmlStr) {
		if (!StringUtil.isNullOrBlank(htmlStr)) {
			htmlStr = Jsoup.clean(htmlStr, Whitelist.none());			
			htmlStr = replaceContent(htmlStr);	
		}
		return htmlStr;
	}

	public static String cleanXmlCharacter(String text) {
		StringBuilder sb = new StringBuilder();
		int length = text.length();
		for (int i = 0; i < length; i++) {
			char c = text.charAt(i);
			if (!isXmlCharacter(c)) {
				c = 0x20;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	private static boolean isXmlCharacter(int c) {
		if (c <= 0xD7FF) {
			if (c >= 0x20) {
				return true;
			} else {
				return c == '\n' || c == '\r' || c == '\t';
			}
		}
		return (c >= 0xE000 && c <= 0xFFFD) || (c >= 0x10000 && c <= 0x10FFFF);
	}
	private static String htmlIframe(String content, String href) {
		Document doc = Jsoup.parse(content);
		Elements iframe = doc.select("iframe");

		for (Element element : iframe) {

			String iframeTag = "";
			try {
				iframeTag = Jsoup
						.connect(getAbsoluteURL(href, element.attr("src")))
						.get().body().html();
			} catch (IOException e) {
				iframeTag = "";
			}

			if (!StringUtil.isNullOrBlank(iframeTag)) {
				iframeTag = getIframeContent(iframeTag, element.attr("src"));
				element.replaceWith((Jsoup.parse(iframeTag)));
			}

		}
		return Jsoup.clean(replaceContent(doc.body().html()),
				getBastWhitelistWitoutIframe());

	}

	private static String replaceContent(String htmlStr) {
		if (!StringUtil.isNullOrBlank(htmlStr)) {
			
			htmlStr = htmlStr.replace("&#183;", "");
			htmlStr = htmlStr.replace("&nbsp;", " ");
			htmlStr = htmlStr.replaceAll("&lt;", "<");
			htmlStr = htmlStr.replaceAll("&gt;", ">");
			htmlStr = htmlStr.replaceAll("&amp;lt;", "<");
			htmlStr = htmlStr.replaceAll("&amp;gt;", ">");
			htmlStr = htmlStr.replace("<#root>", "");
			htmlStr = htmlStr.replace("&quot;", "\"");
		}

		return htmlStr;
	}

	private static String cleanDisplayNone(String htmlStr) {
		Document doc = Jsoup.parse(htmlStr);
		Elements div = doc.select("div[style=display:none;]");
		for (Element element : div) {
			element.remove();
		}
		return doc.body().html();
	}

	public static String getHtml(String href) {
		try {
			return getHtmlContent(Jsoup.connect(href).get().body().html(), href);
		} catch (IOException e) {
			return "URL解析 错误  请重试";
		}
	}

	private static String getIframeContent(String content, String href) {
		if (!StringUtil.isNullOrBlank(content)
				&& !StringUtil.isNullOrBlank(href)) {
			
			content = cleanDisplayNone(content);
			//content = getAbsoluteContent(content, href);
			content = htmlImgWithUrl(content, href);
			content = htmlVideoWithUrl(content, href);
			content = cleanHrefWithUrl(content, href);
			return Jsoup.clean(content, getBastWhitelistWitoutIframe());
		}
		return content;

	}

	private static String getHtmlContent(String content, String href) {
		if (!StringUtil.isNullOrBlank(content)
				&& !StringUtil.isNullOrBlank(href)) {
			content = cleanDisplayNone(content);			
			
			//content = getAbsoluteContent(content, href);
			
			content = htmlImgWithUrl(content, href);
			content = htmlVideoWithUrl(content, href);
			 content = cleanHrefWithUrl(content, href);
			content = htmlIframe(content, href);
			return Jsoup.clean(replaceContent(content), getBastWhitelistWitoutIframe());
		}
		return content;

	}

	private static Whitelist getBastWhitelist() {
		return Whitelist.basic().addAttributes("img", "src")
				.addAttributes("embed", "src").addAttributes("iframe", "src");
	}

	private static Whitelist getBastWhitelistWitoutIframe() {
		return Whitelist.basic().addAttributes("img", "src")
				.addAttributes("embed", "src")
				.addAttributes("span", "classs", "id", "name")
				.addAttributes("div", "classs", "id", "name");
	}

	/**
	 * 去除无效链接
	 * @param htmlStr
	 * @param href
	 * @return
	 * String 
	 * 下午2:49:10
	 * author daijf
	 */
	private static String cleanHrefWithUrl(String htmlStr, String href) {
		// TODO Auto-generated method stub
		Document doc = Jsoup.parse(htmlStr);
		Elements clicks = doc.select("a");
		for (Element element : clicks) {
			if (StringUtil.isNullOrBlank(element.attr("href"))) {
				//element.replaceWith(Jsoup.parse(element.text()));
				element.remove();
			} else if (element.attr("href").startsWith("javascript")) {
				//element.replaceWith(Jsoup.parse(element.text()));
				element.remove();
			}else {
				if((!element.attr("href").startsWith("http://"))&&(!element.attr("href").startsWith("javascript"))){
					
				String tmpHref = getAbsoluteURL(href, element.attr("href"));
				log.info(element.html()+"	href"+element.attr("href")+"	href:"+href +"		tmpHref:"+tmpHref);
				if (!StringUtil.isNullOrBlank(tmpHref)) {
					element.attr("href", tmpHref);
				}
				}
			}

		}
		return doc.body().html();
	}
	private static String getAbsoluteContent(String htmlStr, String href) {
		Document doc = Jsoup.parse(htmlStr,href);
		return doc.body().html();
	}
	

	/**
	 * 解决相对路径问题
	 * 
	 * @param baseURI
	 * @param relativePath
	 * @return String 下午3:33:03 author daijf
	 */
	@SuppressWarnings("finally")
	private static String getAbsoluteURL(String baseURI, String relativePath) {
		String abURL = null;
		try {
			URI base = new URI(baseURI);// 基本网页URI
			URI abs = base.resolve(relativePath);// 解析于上述网页的相对URL，得到绝对URI
			URL absURL = abs.toURL();// 转成URL
			abURL = absURL.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			log.error("[无线统一平台]相对路径", e);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			log.error("[无线统一平台]相对路径", e);
		} finally {
			if (null == abURL) {
				abURL = "";
			}
			return abURL;
		}
	}

	private static String htmlImgWithUrl(String content, String href) {
		Document doc = Jsoup.parse(content);
		Elements img = doc.select("img");
		for (Element element : img) {
			if (!StringUtil.isNullOrBlank(element.attr("src"))) {
				element.attr("src", getAbsoluteURL(href, element.attr("src")));
			}

		}
		return doc.body().html();
	}

	private static String htmlVideo(String content) {
		Document doc = Jsoup.parse(content);
		Elements video = doc.select("embed");
		for (Element element : video) {
			if (!StringUtil.isNullOrBlank(element.attr("src"))) {
				element.append(JsoupUtil.getVideoUrl(element.attr("src")));
			}

		}
		return doc.body().html();
	}

	private static String htmlVideoWithUrl(String content, String href) {
		Document doc = Jsoup.parse(content);
		Elements video = doc.select("embed");
		for (Element element : video) {
			if (!StringUtil.isNullOrBlank(element.attr("src"))) {
				element.attr("src", getAbsoluteURL(href, element.attr("src")));
			}

		}
		return doc.body().html();
	}

	private static String getVideoUrl(String url) {
		return url;
	}

	private static String cleanHref(String htmlStr) {
		Document doc = Jsoup.parse(htmlStr);
		Elements clicks = doc.select("a");
		for (Element element : clicks) {
			if (StringUtil.isNullOrBlank(element.attr("href"))) {
				element.remove();
			} else if (!element.attr("href").startsWith("http")) {
				element.remove();
			}

		}
		return doc.body().html();
	}



	private static String cleanWhitelistBasic(String htmlStr) {
		String textStr = "";
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		java.util.regex.Pattern p_table;
		java.util.regex.Matcher m_table;

		htmlStr = htmlStr.replaceAll("&lt;", "<");
		htmlStr = htmlStr.replaceAll("&gt;", ">");

		htmlStr = htmlStr.replaceAll("&amp;lt;", "<");
		htmlStr = htmlStr.replaceAll("&amp;gt;", ">");

		try {
			// }
			String regEx_html = "<[^a|^A|^(/a)|^(/A)][^>]*>"; // 定义HTML标签的正则表达式

			String regEx_table = "</[^a|^A][^>]*>";

			p_table = Pattern.compile(regEx_table, Pattern.CASE_INSENSITIVE);
			m_table = p_table.matcher(htmlStr);
			htmlStr = m_table.replaceAll(""); // 过滤html标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
		}

		return textStr;// 返回文本字符串
	}



}
