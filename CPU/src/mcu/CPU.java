package mcu;

import java.io.IOException;

public class CPU {
	
	public enum EState {
		eStopped,
		eRunning
	}

	public enum EOperator {
		eHalt,
		eLoada,
		eLoadc,
		eStorea,
		eAdda,
		eAddc,
		eSuba,
		eJump,
		ePush,
		ePop,
		eGej,
		eAllocate,
		eDividea,
		eInterrupt,
		eLoado,
		eAddo
	}
	
	private Memory memory;
	
	public void associate(Memory memory) {
		this.memory = memory;
	}
	
	private EState eState;
	private IR ir;
	public Register mar, mbr;
	public Register pc, ac; 
	public Register sp, hp;
	public Register cs, ds, ss, hs;
	
	public CPU() throws IOException {
		pc = new Register();
		ir = new IR();	
		mar = new Register();	
		mbr = new Register();	
		ac = new Register();
		sp = new Register();
		hp = new Register();
		cs = new Register();
		ds = new Register();
		ss = new Register();
		hs = new Register();
	}
	
	public void start() {
		this.eState = EState.eRunning;
		this.run();
	}
	
	public void run() {
		System.out.println("<------Instruction Cycle------>");
		while(this.eState == EState.eRunning) {
			this.fetch();
			this.decode();
			this.execute();
			// checkInterrupt();
		}
		// print result
	}
	
	private void fetch() {
		// MAR <= CS + PC
		mar.setValue(memory.storage[cs.getValue()].getValue(pc.getValue()));
		
		// MBR = memory.load();
		memory.load(cs);
		
		// IR = MBR
		ir.setValue(mbr.getValue());
	}
	
	private void decode() {
		// read Operator
	}
	
	private void checkInterrupt() {
	}

	private void execute() {
		pc.setValue(pc.getValue() + 1);
		
		switch (ir.geteOperator()) {
		case eHalt:
			this.halt();
		 	break;
		case eLoada:
			this.loada();
		 	break;
		case eLoadc:
			this.loadc();
			break;
		case eStorea:
			this.storea();
			break;
		case eAdda:
			this.adda();
			break;
		case eAddc:
			this.addc();
			break;
		case eSuba:
			this.suba();
			break;
		case eJump:
			this.jump();
			break;
		case ePush:
			this.push();
			break;
		case ePop:
			this.pop();
			break;
		case eGej:
			this.gej();
			break;
		case eAllocate:
			this.allocate();
			break;
		case eDividea:
			this.dividea();
			break;
		case eInterrupt:
			this.interrupt();
			break;
		case eLoado:
			this.loado();
			break;
		case eAddo:
			this.addo();
			break;
		default:
			break;
		 }
	}

	public void halt() {
		this.eState = EState.eStopped;
	}
	
	public void loada() {
		String[] divideResult = divideOperand(ir.getOperand());
		mar.setValue(Integer.parseInt(divideResult[1]));
		
		Register sr = null;
		if(divideResult[0] == "11") {
			sr = this.ds;
		}
		else if(divideResult[0] == "22") {
			sr = this.ss;
		}
		else if(divideResult[0] == "33") {
			sr = this.hs;
		}
		else if(divideResult[0] == "44") {
			sr = null;
		}
		memory.load(sr);
		ac.setValue(mbr.getValue());
	}
	
	private String[] divideOperand(int operand) {
		String operandStr = Integer.toString(operand);
		String[] result = new String[2];
		result[0] = operandStr.substring(0, 2);
		result[1] = operandStr.substring(2);
		return result;
	}

	public void loadc() {
		ac.setValue(ir.getOperand());
	}
	
	public void storea() {
		String[] divideResult = divideOperand(ir.getOperand());
		mar.setValue(Integer.parseInt(divideResult[1]));
		
		mbr.setValue(ac.getValue());
		
		Register sr = null;
		if(divideResult[0] == "11") {
			sr = this.ds;
		}
		else if(divideResult[0] == "22") {
			sr = this.ss;
		}
		else if(divideResult[0] == "33") {
			sr = this.hs;
		}
		else if(divideResult[0] == "44") {
			sr = null;
		}
		memory.store(sr);
	}
	
	public void adda() {
		String[] divideResult = divideOperand(ir.getOperand());
		mar.setValue(Integer.parseInt(divideResult[1]));
	
		Register sr = null;
		if(divideResult[0] == "11") {
			sr = this.ds;
		}
		else if(divideResult[0] == "22") {
			sr = this.ss;
		}
		else if(divideResult[0] == "33") {
			sr = this.hs;
		}
		
		memory.load(sr);
		ac.setValue(ac.getValue() + mbr.getValue());
	}
	
	public void addc() {
		ac.setValue(ac.getValue() + ir.getOperand());
	}
	
	public void suba() {
		String[] divideResult = divideOperand(ir.getOperand());
		mar.setValue(Integer.parseInt(divideResult[1]));
		
		Register sr = null;
		if(divideResult[0] == "11") {
			sr = this.ds;
		}
		else if(divideResult[0] == "22") {
			sr = this.ss;
		}
		else if(divideResult[0] == "33") {
			sr = this.hs;
		}
		
		memory.load(sr);
		ac.setValue(ac.getValue() - mbr.getValue());
	}
	
	public void jump() {
		pc.setValue(ir.getOperand());
	}

	public void push() {
		ac.setValue(ir.getOperand());
		sp.setValue(ac.getValue());
	}

	public void pop() {
		int newSp = memory.pop(sp.getValue(), ir.getOperand());
		sp.setValue(newSp);
	}

	public void gej() {
		if(ac.getValue() >= 0) {
			pc.setValue(ir.getOperand());
		}
	}
	
	private void allocate() {
		String[] divideResult = divideOperand(ir.getOperand());
		
		Register sr = null;
		if(divideResult[0] == "11") {
			sr = this.ds;
		}
		else if(divideResult[0] == "22") {
			sr = this.ss;
		}
		else if(divideResult[0] == "33") {
			sr = this.hs;
		}
		
		int address = Integer.parseInt(divideResult[1]);
		hp.setValue(memory.allocate(hp.getValue(), sr, address));
	}
	
	private void dividea() {
		String[] divideResult = divideOperand(ir.getOperand());
		mar.setValue(Integer.parseInt(divideResult[1]));
		
		Register sr = null;
		if(divideResult[0] == "11") {
			sr = this.ds;
		}
		else if(divideResult[0] == "22") {
			sr = this.ss;
		}
		else if(divideResult[0] == "33") {
			sr = this.hs;
		}
		
		memory.load(sr);
		ac.setValue(ac.getValue() / mbr.getValue());
	}
	
	private void interrupt() {
		memory.IOInterrupt(ir.getOperand());
	}
	
	public class Register {
		protected int value;

		public Register() {
			this.value = 0;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public void setValue(int value) {
			this.value = value;
		}
	}
	
	private void loado() {
		memory.load(ir.getOperand()/100, (ir.getOperand() / 10) % 10, ir.getOperand() % 10);
		ac.setValue(mbr.getValue());
	}
	
	private void addo() {
		memory.load(ir.getOperand()/100, (ir.getOperand() / 10) % 10, ir.getOperand() % 10);
		ac.setValue(ac.getValue() + mbr.getValue());
	}
	
	private class IR extends Register {

		public IR() {
			super();
		}

		public EOperator geteOperator() {
			String firstWord = String.valueOf(value).split(" ")[0];
			Integer opcode = Integer.decode(firstWord);
			EOperator eOperator = EOperator.values()[opcode];
			System.out.println("operator : " + eOperator);
			return eOperator;
		}
		
		public int getOperand() {
			String secodeWord = String.valueOf(value).split(" ")[1];
			Integer operand = Integer.parseInt(secodeWord);
			System.out.println("operand : " + operand);
			return operand;
		}
	}
}
