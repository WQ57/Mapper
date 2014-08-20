package com.wq.common.vo;

import java.util.Date;

public class TestVO_02 implements java.io.Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 2265160715168086948L;

	private String s;

	private Integer i;

	private Long l;

	private Float f;

	private Double d;

	private Date date;

	/**
	 * @return the s
	 */
	public String getS() {
		return s;
	}

	/**
	 * @param s
	 *            the s to set
	 */
	public void setS(String s) {
		this.s = s;
	}

	/**
	 * @return the i
	 */
	public Integer getI() {
		return i;
	}

	/**
	 * @param i
	 *            the i to set
	 */
	public void setI(Integer i) {
		this.i = i;
	}

	/**
	 * @return the l
	 */
	public Long getL() {
		return l;
	}

	/**
	 * @param l
	 *            the l to set
	 */
	public void setL(Long l) {
		this.l = l;
	}

	/**
	 * @return the f
	 */
	public Float getF() {
		return f;
	}

	/**
	 * @param f
	 *            the f to set
	 */
	public void setF(Float f) {
		this.f = f;
	}

	/**
	 * @return the d
	 */
	public Double getD() {
		return d;
	}

	/**
	 * @param d
	 *            the d to set
	 */
	public void setD(Double d) {
		this.d = d;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestVO_02 [s=" + s + ", i=" + i + ", l=" + l + ", f=" + f
				+ ", d=" + d + ", date=" + date + "]";
	}

}
