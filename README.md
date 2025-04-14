3 data tables (Firebase):
	1. Items List - {ItemNumber: char(10), ItemName: char(30), Price: 999.99}
	2. Invoice Info - {*InvoiceNumber: char(10), InvoiceDate: date(10), **InvoiceTotal: 999999.99}
	3. Invoice Data - {*InvoiceNumber: char(10), ItemNumber: char(10), Price: 999.99, ItemQuantity: 999, **ItemTotal: 999999.99}


Supported actions:
	1. Option to add a new invoice
	2. Option to print invoice by InvoiceNumber (fetch from Invoice Info & Invoice Data tables)
	3. Option to print invoice by selected date ()

Includes:
	-Activities 
	-Buttons
	-TextView
	-EditTexts
	-Data is Saved in a firebase database
	-Add action
	-Update action
	-Search action
	-Design 
