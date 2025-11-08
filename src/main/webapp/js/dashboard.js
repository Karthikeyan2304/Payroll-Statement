$(document).ready(function() {

	// YTD Payroll AJAX Call

	$("#ytd-tab").on("click", function() {
		$.ajax({
			url: "getYeartoDatePayroll",
			type: "GET",
			dataType: "json",
			success: function(data) {
				let tbody = $("#ytdTable tbody");
				tbody.empty();

				if (data && data.length > 0) {
					$.each(data, function(index, payroll) {
						tbody.append(`
                            <tr class="payroll-row" data-payroll='${JSON.stringify(payroll)}'>
                                <td>${index + 1}</td>
                                <td>${payroll.empCode}</td>
                                <td>${payroll.name}</td>
                                <td>${payroll.payrollMonth}</td>
                                <td>${payroll.basicPay}</td>
                                <td>${payroll.hra}</td>
                                <td>${payroll.allowances}</td>
                                <td>${payroll.deductions}</td>
                                <td>${payroll.tax}</td>
                                <td>${payroll.grossPay}</td>
                                <td>${payroll.netPay}</td>
                                <td>${payroll.status}</td>
                            </tr>
                        `);
					});
				} else {
					tbody.append("<tr><td colspan='12' class='text-center'>No records found</td></tr>");
				}
			},
			error: function(xhr, status, error) {
				console.error("Error fetching YTD Payroll:", error);
			}
		});
	});

	// Current Month Payroll AJAX Call
	$("#current-tab").on("click", function() {
		$.ajax({
			url: "getCurrentMonthPayroll",
			type: "GET",
			dataType: "json",
			success: function(data) {
				let tbody = $("#currentTable tbody");
				tbody.empty();

				if (data && data.length > 0) {
					$.each(data, function(index, payroll) {
						tbody.append(`
                            <tr class="payroll-row" data-payroll='${JSON.stringify(payroll)}'>
                                <td>${index + 1}</td>
                                <td>${payroll.empCode}</td>
                                <td>${payroll.name}</td>
                                <td>${payroll.payrollMonth}</td>
                                <td>${payroll.basicPay}</td>
                                <td>${payroll.hra}</td>
                                <td>${payroll.allowances}</td>
                                <td>${payroll.deductions}</td>
                                <td>${payroll.tax}</td>
                                <td>${payroll.grossPay}</td>
                                <td>${payroll.netPay}</td>
                                <td>${payroll.status}</td>
                            </tr>
                        `);
					});
				} else {
					tbody.append("<tr><td colspan='12' class='text-center'>No records found</td></tr>");
				}
			},
			error: function(xhr, status, error) {
				console.error("Error fetching Current Month Payroll:", error);
			}
		});
	});

	// Previous Month Payroll AJAX Call

	$("#previous-tab").on("click", function() {
		$.ajax({
			url: "getPreviousMonthPayroll",
			type: "GET",
			dataType: "json",
			success: function(data) {
				let tbody = $("#previousTable tbody");
				tbody.empty();

				if (data && data.length > 0) {
					$.each(data, function(index, payroll) {
						tbody.append(`
                            <tr class="payroll-row" data-payroll='${JSON.stringify(payroll)}'>
                                <td>${index + 1}</td>
                                <td>${payroll.empCode}</td>
                                <td>${payroll.name}</td>
                                <td>${payroll.payrollMonth}</td>
                                <td>${payroll.basicPay}</td>
                                <td>${payroll.hra}</td>
                                <td>${payroll.allowances}</td>
                                <td>${payroll.deductions}</td>
                                <td>${payroll.tax}</td>
                                <td>${payroll.grossPay}</td>
                                <td>${payroll.netPay}</td>
                                <td>${payroll.status}</td>
                            </tr>
                        `);
					});
				} else {
					tbody.append("<tr><td colspan='12' class='text-center'>No records found</td></tr>");
				}
			},
			error: function(xhr, status, error) {
				console.error("Error fetching Previous Month Payroll:", error);
			}
		});
	});

	// Modal Row Click Event 
	$(document).on("click", ".payroll-row", function() {
		let payroll = $(this).data("payroll");
		showPayrollModal(payroll);
	});





	// Modal Display Function

	function showPayrollModal(payroll) {
		let modalBody = $("#payrollModalBody");
		modalBody.empty();

		for (const [key, value] of Object.entries(payroll)) {
			modalBody.append(`
            <tr>
                <th style="width:20%;">${key.toUpperCase()}</th>
                <td>${value}</td>
            </tr>
        `);
		}
		// Store payroll data in the Download button
		//For Excel
		$("#modalDownloadBtnExcel").data("payroll", payroll);

		//For PDF
		$("#modalDownloadBtnPDF").data("payroll", payroll);

		let modal = new bootstrap.Modal(document.getElementById('payrollModal'));
		modal.show();
	}

	//modal download function for excel

	$(document).on('click', '#modalDownloadBtnExcel', function() {
		let payroll = $(this).data("payroll");
		exportJsonToExcel(payroll);
	})

	//modal download function for pdf
	$(document).on('click', '#modalDownloadBtnPDF', function() {
		let payroll = $(this).data("payroll");
		exportJsonToPDF(payroll);
	})

});


//view excel document download 
function exportJsonToExcel(payroll) {
	//document.location.href="/downloadPdf"
	if (payroll) {
		console.log(payroll);
		const payrollData = [payroll];
		const uppercasedKeysArray = payrollData.map(obj => {
			return Object.keys(obj).reduce((acc, key) => {
				acc[key.toUpperCase()] = obj[key];
				return acc;
			}, {});
		});
		const workbook = XLSX.utils.book_new();
		const worksheet = XLSX.utils.json_to_sheet(uppercasedKeysArray);
		XLSX.utils.book_append_sheet(workbook, worksheet, "Payroll");
		const fileName = `${payroll.empCode}_${payroll.payrollMonth}_Payroll.xlsx`;
		XLSX.writeFile(workbook, fileName);

	}
}
//view PDF document download 
function exportJsonToPDF(payroll) {
	if (payroll) {
		const { jsPDF } = window.jspdf;
		const doc = new jsPDF();

		const leftColX = 20;
		const rightColX = 80;
		let yOffset = 20;
		doc.setFont("helvetica", "bold");
		doc.setFontSize(16);
		doc.text("Employee Payroll Details", 70, 10);

		doc.setFontSize(12);
		doc.setFont("helvetica", "normal");

		for (const key in payroll) {
			if (Object.hasOwnProperty.call(payroll, key)) {
				const label = `${key.toUpperCase()} :`;
				const value = payroll[key] ? String(payroll[key]) : "";


				doc.setFont("helvetica", "bold");
				doc.text(label, leftColX, yOffset);

				// Draw value
				doc.setFont("helvetica", "normal");


				const splitValue = doc.splitTextToSize(value, 100);
				doc.text(splitValue, rightColX, yOffset);

				yOffset += (splitValue.length * 8) + 4;
				if (yOffset > 280) {
					doc.addPage();
					yOffset = 20;
				}
			}
		}
		const fileName = `${payroll.empCode || "Employee"}_${payroll.payrollMonth || "Month"}_Payroll.pdf`;
		doc.save(fileName);
	}
}



