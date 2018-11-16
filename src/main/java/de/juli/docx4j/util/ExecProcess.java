package de.juli.docx4j.util;

import java.io.IOException;
import java.nio.file.Path;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecProcess extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(ExecProcess.class);
	
	private static Process process; 
	private static ExecProcess extProcess;
	private Executer exec; 
	private String param;
	
	private ExecProcess() {
	}
	
	private ExecProcess(Executer exec, String param) {
		this();
		this.param = param;
		this.exec = exec;
	}

	private ExecProcess(Executer exec, Path param) {
		this(exec, param.toString());
	}

	
	public static ExecProcess getInstance(Executer exec, Path param) {
		if(extProcess == null) {
			extProcess = new ExecProcess(exec, param);
		}
		return extProcess;
	}

	public static ExecProcess getInstance(Executer exec, String param) {
		if(extProcess == null) {
			extProcess = new ExecProcess(exec, param);
		}
		return extProcess;
	}
	
	private void startProcess() {
		try {
			process = new ProcessBuilder(exec.getExecuter(), param).start();
			stopProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopProcess() {
		if(process.isAlive()) {
			process.destroy();
		}
	}

	@Override
	public void run() {
		startProcess();
		LOG.info("Started");
	}
	
	@PreDestroy
	public void tearDown() {
		LOG.info("{} goes down", this.getClass().getName());
	}

	public static Process getProcess() {
		return process;
	}

	public static void setProcess(Process process) {
		ExecProcess.process = process;
	}
	
	
}