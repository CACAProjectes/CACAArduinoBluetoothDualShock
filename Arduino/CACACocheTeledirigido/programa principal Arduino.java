
int cIntIzqOn  = b00010000;
int cIntIzqOff = b11101111;
int cIntDerOn  = b00000001;
int cIntDerOff = b11111110;
int cSirRojOn  = b00001000;
int cSirRojOff = b11110111;
int cSirAzuOn  = b00000010;
int cSirAzuOff = b11111101;
int cLuzCru    = b00000100;


int vValor = b00000000;

loop {
	int vOpcion = readBluetooth()
	//
	calcularInt(1);		// on
	calcularSirena(1);	// blue	
	writeShift(vValor);
	delay(200);
	//
	calcularSirena(0);	// null	
	writeShift(vValor);
	delay(100);
	//
	calcularSirena(1);	// blue	
	writeShift(vValor);
	delay(200);
	//
	calcularInt(0);		// off
	calcularSirena(2);	// red
	writeShift(vValor);
	delay(200);
	//
	calcularSirena(2);	// red
	writeShift(vValor);
	delay(100);
	//
	calcularSirena(2);	// red
	writeShift(vValor);
	delay(200);
	//
}

void calcularInt(int pOpcion) {
	//
	if (vOpcion == 'a' && pOpcion == 1) {		// on 
		// a=INT IZQ on
		vValor = vValor | cIntIzqOn;
	}
	else if (vOpcion == 'b' || pOpcion == 0) {	// off	
		// b=INT IZQ off
		vValor = vValor & cIntIzqOff;
	}	
	else if (vOpcion == 'i' && pOpcion == 1) {	// on 
		// a=INT DER on
		vValor = vValor | cIntDerOn;
	}
	else if (vOpcion == 'j' || pOpcion == 0) {	// off	
		// b=INT DER off
		vValor = vValor & cIntDerOff;
	}	
}

void calcularSirena(int pOpcion) {
	//
	if (vOpcion == 'c' && pOpcion == 1) {		// on BLUE
		// a=SIR AZU on
		vValor = vValor | cSirAzuOn;
		vValor = vValor & cSirRojOff;
	}
	else if (vOpcion == 'c' && pOpcion == 2) {	// on RED
		// a=SIR ROJ on
		vValor = vValor | cSirRojOn;
		vValor = vValor & cSirAzuOff;
	}
	else if (vOpcion == 'd' || pOpcion == 0) {	// off	
		// b=SIR off
		vValor = vValor & cSirAzuOff;
		vValor = vValor & cSirRojOff;
	}	
}