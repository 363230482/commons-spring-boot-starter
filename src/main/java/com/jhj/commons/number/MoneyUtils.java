package com.jhj.commons.number;


import com.jhj.commons.validate.CommonValidateUtil;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对common包中JhjMoneyUtil的补充
 * 
 * @author ZhangBo
 *
 */
public final class MoneyUtils {

	/**
	 * 最多两位小数
	 */
	public static final String MONEY_REG_EXP = "^\\d+(?:.\\d{1,2})?$";
	
	public static final Pattern MONEY_PATTERN = Pattern.compile(MONEY_REG_EXP);

	private MoneyUtils() {
	}

	public static String getMoneyString(Integer cent) {
		return cent == null ? null : getMoneyString(cent.longValue());
	}

	public static String getMoneyString(Long cent) {
		return cent == null ? null : getMoneyString(cent.longValue());
	}

	/**
	 * 把分转为元
	 *
	 * @param cent
	 * @return
	 */
	public static String getMoneyString(long cent) {
		
		boolean minus = false;
		if (cent < 0) {
			minus = true;
			cent = Math.abs(cent);
		}
		long yuan = cent / 100;
		long jiao = cent / 10 - yuan * 10;
		long fen = cent - yuan * 100 - jiao * 10;
		String moneyString =  yuan + "." + jiao + fen;

		return minus ? "-" + moneyString : moneyString;
	}

	/**
	 * 验证金额字符串，最多保留2位小数
	 * 
	 * @param money
	 * @return
	 */
	public static boolean validateMoney(String money) {
		if (CommonValidateUtil.isEmpty(money)) {
			return false;
		}
		
		Matcher matcher = MONEY_PATTERN.matcher(money);
		return matcher.matches();
	}
	
	/**
	 * 元转分
	 * 
	 * @param yuan
	 * @return
	 */
	public static int yuan2fen(String yuan) {
		if (!validateMoney(yuan)) {
			throw new IllegalArgumentException("金额格式不正确,最多2位小数: " + yuan);
		}
		return new BigDecimal(yuan).multiply(new BigDecimal("100")).intValue();
	}
}
