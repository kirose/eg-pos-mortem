package com.fimet.simulator.model;

import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class MasterCard extends AbstractSimulatorModel {

	public MasterCard () {
		super("MasterCard");
	}

	/**
	* @param IMessage msg the outgoing message
	**/
	@Override
	public IMessage simulateRequest(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0100".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			return msg;
		}
		if ("0120".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			return msg;
		}
		if ("0400".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"35");
			return msg;
		}
		if ("0800".equals(mti)){
			msg.setValue("2","07867");
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"11");
			msg.setValue("33","007867");
			msg.setValue("70","270");
			msg.setValue("94","0B0    ");
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
		if ("0800".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			msg.setValue("39","00");
			msg.setValue("63","MCCKJONGU");
			msg.setValue("94","0B0    ");
			return msg;
		}
		if ("0100".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","14","18","22","23","42","43","47","61","35","43","53","55","55.5F2A","55.95","55.9A","55.9F09","55.9F10","55.9F1A","55.9F1E","55.9F26","55.9F27","55.9F33","55.9F34","55.9F35","55.9F36","55.9F37","55.9F41","55.9F53","61");
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			msg.setValue("48","R26032178701P8801Y920309433540101M0216549138005211069003042005061150139059239080204");
			msg.setValue("63","MCWAHG1DW");
			msg.setValue("124","2020000009001150715491389999001300   000000BNMR    0000000000051V0                    19 0                    ");
			return msg;
		}
		if ("0200".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","14","18","22","23","35","42","43","52","61");
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			com.fimet.simulator.field.SetRandom9N.getInstance().simulate(msg,"63");
			return msg;
		}
		if ("0302".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","14","18","22","23","38","42","43","61");
			msg.setValue("39","00");
			return msg;
		}
		if ("0400".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","14","18","22","23","38","42","43","61");
			msg.setValue("39","00");
			return msg;
		}
		if ("0420".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("12","13","14","18","22","28","38","42","43","60","61","90");
			msg.setValue("39","00");
			return msg;
		}
		return null;
	}
}
