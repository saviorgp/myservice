package com.myservice.model.transaction;

public interface ITransaction {
	public void execute() throws Exception;
	public void updateView();
}
