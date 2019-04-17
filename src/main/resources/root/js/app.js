$(document).ready(function () {
	function deleteScreenshot(screen) {
		$.ajax({
			url: "/screenshot/" + screen.id,
			method: "DELETE"
		}).done(function () {
			reload();
		}).fail(function () {
			$("#delete-failed-alert").slideDown().delay(1500).slideUp();
		});
	}

	function createButtons(elem, screen) {
		var group = $("<div class=\"btn-group\" role=\"group\" />");
		var btnDelete = $("<button type='button' class='btn btn-danger'><i class='fa fa-trash-alt'></i></button>");
		btnDelete.click(function() {
			deleteScreenshot(screen);
		});
		group.append(btnDelete);
		elem.append(group);
	}

	function reload() {
		$.get("/screenshot").done(function (data) {
			$("#main-table tbody tr").remove();
			data.forEach(function(screenshot) {
				var $tr = $("<tr />");
				var id = $("<td scope='col'/>");
				var idA = $("<a />");
				idA.attr("href", "/screenshot/" + screenshot.id + "/image");
				idA.attr("target", "_blank");
				idA.text(screenshot.id);
				id.append(idA);
				$tr.append(id);
				var url = $("<td scope='col'/>");
				url.text(screenshot.url);
				$tr.append(url);
				var auth = $("<td scope='col'/>");
				auth.text(screenshot.authenticationInformation.authenticationType);
				$tr.append(auth);
				var interval = $("<td scope='col'/>");
				interval.text(screenshot.intervalSeconds);
				$tr.append(interval);
				var actions = $("<td scope='col'/>");
				createButtons(actions, screenshot);
				$tr.append(actions);
				$("#main-table tbody").append($tr);
			})
		});
	}

	reload();

	$("#saveNewScreenshot").click(function () {
		var url = $("#url1").val();
		var loginUrl = $("#loginUrl1").val();
		var interval = $("#intervalSeconds1").val();
		var authType = $("#authType1").val();
		var username = $("#username1").val();
		var password = $("#password1").val();
		var usernameSelector = $("#usernameSelector1").val();
		var passwordSelector = $("#passwordSelector1").val();
		var submitSelector = $("#submitSelector1").val();

		var authenticationInformation = {
			"authenticationType": "NONE",
			"type": "none"
		};
		if (authType === "BASIC") {
			authenticationInformation = {
				"authenticationType": authType,
				"username": username,
				"password": password,
				"type": "basic"
			}
		} else if (authType === "FORM") {
			authenticationInformation = {
				"authenticationType": authType,
				"username": username,
				"password": password,
				"usernameSelector": usernameSelector,
				"passwordSelector": passwordSelector,
				"submitSelector": submitSelector,
				"type": "form"
			}
		}

		var obj = {
			"url": url,
			"loginUrl": loginUrl,
			"intervalSeconds": interval,
			authenticationInformation: authenticationInformation
		};

		$.ajax({
			url: "/screenshot",
			method: "POST",
			data: JSON.stringify(obj),
			contentType: 'application/json'
		}).done(function() {
			reload();
		}).fail(function () {
			$("#create-failed-alert").slideDown().delay(1500).slideUp();
		})
	})
});