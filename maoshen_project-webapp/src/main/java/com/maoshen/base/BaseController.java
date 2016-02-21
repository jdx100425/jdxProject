/**   
 * @Description:(用一句话描述该类做什么)
 * @author Daxian.jiang
 * @Email  Daxian.jiang@vipshop.com
 * @Date 2015年9月17日 上午10:58:30
 * @Version V1.0   
 */
package com.maoshen.base;

import org.apache.log4j.Logger;

public class BaseController {
    private static final Logger LOGGER = Logger.getLogger(BaseController.class);

    public String redirectAdminPage() {
        return "redirect:http://admin.maoshen.com:8080/admin/index";
    }
}