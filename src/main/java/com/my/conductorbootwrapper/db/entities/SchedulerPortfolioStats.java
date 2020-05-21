package com.my.conductorbootwrapper.db.entities;

import java.text.DecimalFormat;

import org.springframework.lang.Nullable;

import com.google.gson.Gson;

public class SchedulerPortfolioStats {
	
	private long total;
	private long success;
	private long failed;
	
	@Nullable
	private double percentage;

	public SchedulerPortfolioStats(long total, long success, long failed) {
		super();
		this.total = total;
		this.success = success;
		this.failed = failed;
		
		if(total > 0)
		{
			this.percentage = success / total * 100;
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			this.percentage = Double.parseDouble(df.format(this.percentage));
		}
		else
			this.percentage = Double.parseDouble("100.00");
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getSuccess() {
		return success;
	}

	public void setSuccess(long success) {
		this.success = success;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "SchedulerPortfolioStats [total=" + total + ", success=" + success + ", failed=" + failed
				+ ", percentage=" + percentage + "]";
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
