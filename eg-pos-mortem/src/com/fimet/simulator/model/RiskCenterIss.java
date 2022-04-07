package com.fimet.simulator.model;

import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class RiskCenterIss extends AbstractSimulatorModel {

	public RiskCenterIss () {
		super("RiskCenterIss");
	}

	/**
	* @param IMessage msg the outgoing message
	**/
	@Override
	public IMessage simulateRequest(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0800".equals(mti)){
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"11");
			msg.setValue("15","0903");
			msg.setValue("48","0111000122S00484");
			msg.setValue("70","301");
			return msg;
		}
		return msg;
	}

	/**
	* @param IMessage msg is the incoming message
	**/
	@Override
	public IMessage simulateResponse(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0200".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("6","18","43","48","51","60","62","63.Q1","63.Q2","63.Q3","63.Q6","63.B2","63.B3","63.B4","63.C6","63.C4","63.C0","127");
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"15");
			msg.setValue("38","XXXXXX");
			msg.setValue("39","XX");
			msg.setValue("44","00");
			msg.setValue("63.RC","1000000199011100122INFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICI");
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"17");
			return msg;
		}
		if ("0220".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("6","12","13","15","17","18","38","43","51","60","62","63","127");
			msg.setValue("39","00");
			msg.setValue("63.RC","1000000199INFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONAL");
			return msg;
		}
		if ("0221".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("6","12","13","15","17","38","43","48","51","60","62","63","127");
			msg.setValue("39","00");
			msg.setValue("63.RC","1000000199INFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONAL");
			return msg;
		}
		if ("0420".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("6","12","13","15","17","38","43","48","51","60","62","63","127");
			msg.setValue("39","XX");
			msg.setValue("63.RC","100000019901112INFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONAL");
			return msg;
		}
		if ("0421".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("6","12","13","15","17","38","43","48","51","60","62","63","127");
			msg.setValue("39","00");
			msg.setValue("63.RC","1000000199INFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONALINFORMACIONADICIONAL");
			return msg;
		}
		if ("0800".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.setValue("39","00");
			return msg;
		}
		return null;
	}
}
