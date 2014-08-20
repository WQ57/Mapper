package com.wq.common.vo;

import java.util.Date;

import com.wq.common.mapper.Mapper;

public class TestVO_01 implements java.io.Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = -8669610313641766412L;

	@Mapper("String")
	private String s;

	@Mapper("Integer")
	private Integer i;

	@Mapper("Long")
	private Long l;

	@Mapper("Float")
	private Float f;

	@Mapper("Double")
	private Double d;

	@Mapper("Date")
	private Date date;

	@Mapper("TestVO_02")
	private TestVO_02 vo;

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

	/**
	 * @return the vo
	 */
	public TestVO_02 getVo() {
		return vo;
	}

	/**
	 * @param vo
	 *            the vo to set
	 */
	public void setVo(TestVO_02 vo) {
		this.vo = vo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestVO_01 [s=" + s + ", i=" + i + ", l=" + l + ", f=" + f
				+ ", d=" + d + ", date=" + date + ", vo=" + vo + "]";
	}

}
