$(document).ready(function () {
	function deleteScreenshot(id) {
		$.ajax({
			url: "/screenshot/" + id,
			method: "DELETE"
		}).done(function () {
			reload();
		}).fail(function () {
			$("#delete-failed-alert").slideDown().delay(1500).slideUp();
		});
	}

	function stopSession(id) {
		$.ajax({
			url: "/session/" + id,
			method: "DELETE"
		}).done(function() {
			reloadButtons(id);
		}).fail(function () {
			$("#stop-failed-alert").slideDown().delay(1500).slideUp();
		});
	}

	function startSession(id) {
		$.ajax({
			url: "/session/" + id,
			method: "POST"
		}).done(function() {
			reloadButtons(id);
		}).fail(function () {
			$("#start-failed-alert").slideDown().delay(1500).slideUp();
		});
	}

	function restartSession(id) {
		$.ajax({
			url: "/session/" + id,
			method: "PUT"
		}).done(function() {
			reloadButtons(id);
		}).fail(function () {
			$("#restart-failed-alert").slideDown().delay(1500).slideUp();
		});
	}

	function reloadButtons(id, elem) {
		if(elem == null)
			elem = $("#buttons-" + id);

		$("button", elem).remove();

		var btnDelete = $("<button type='button' class='btn btn-danger' title='delete'><i class='fa fa-trash-alt'></i></button>");
		btnDelete.click(function() {
			deleteScreenshot(id);
		});
		elem.append(btnDelete);
		btnDelete.tooltip({placement: 'bottom'});

		$.get("/session/" + id).done(function (data) {
			if(data.state === "running") {
				var btnStop = $("<button type='button' class='btn btn-danger' title='Stop'><i class='fa fa-power-off'></i></button>");
				btnStop.click(function () {
					stopSession(id);
				});
				elem.append(btnStop);
				btnStop.tooltip({placement: 'bottom'});

				var btnRestart = $("<button type='button' class='btn btn-warning' title='Restart'><i class='fa fa-redo'></i></button>");
				btnRestart.click(function () {
					restartSession(id);
				});
				elem.append(btnRestart);
				btnRestart.tooltip({placement: 'bottom'});
			} else {
				var btnStart = $("<button type='button' class='btn btn-warning' title='Start'><i class='fa fa-play'></i></button>");
				btnStart.click(function () {
					startSession(id);
				});
				elem.append(btnStart);
				btnStart.tooltip({placement: 'bottom'});
			}
		});
	}

	function createButtons(elem, screen) {
		var group = $("<div class=\"btn-group\" role=\"group\" id='buttons-" + screen.id + "' />");
		elem.append(group);
		reloadButtons(screen.id, group);
	}

	function reload() {
		$.get("/screenshot").done(function (data) {
			$("#main-table tbody tr").remove();
			$(".tooltip").remove();
			data.forEach(function(screenshot) {
				var $tr = $("<tr class='table-column' />");

				var id = $("<td scope='col'/>");
				var idA = $("<a />");
				var imageLink = "/screenshot/" + screenshot.id + "/image";
				idA.attr("href", imageLink);
				idA.attr("target", "_blank");
				var idI = $("<img style='height: 50px; width: auto;' />");
				idI.attr('src', imageLink);
				idA.append(idI);
				id.append(idA);
				$tr.append(id);

				var name = $("<td scope='col' class='url-col'/>");
				name.text(screenshot.name);
				$tr.append(name);

				var url = $("<td scope='col' class='url-col'/>");
				url.text(screenshot.url);
				$tr.append(url);

				var auth = $("<td scope='col'/>");
				auth.text(screenshot.authenticationInformation.authenticationType);
				$tr.append(auth);

				var interval = $("<td scope='col'/>");
				interval.text(screenshot.intervalSeconds);
				$tr.append(interval);

				var autostart = $("<td scope='col'/>");
				if(screenshot.autostart === true) {
					autostart.append($("<i class='fa fa-check'/>"));
				} else {
					autostart.append($("<i class='fa fa-times'/>"));
				}
				$tr.append(autostart);

				var actions = $("<td scope='col'/>");
				createButtons(actions, screenshot);
				$tr.append(actions);
				$("#main-table tbody").append($tr);
			})
		});
	}

	reload();

	$("#saveNewScreenshot").click(function () {
		var name = $("#name1").val();
		var url = $("#url1").val();
		var loginUrl = $("#loginUrl1").val();
		var interval = $("#intervalSeconds1").val();
		var authType = $("#authType1").val();
		var username = $("#username1").val();
		var password = $("#password1").val();
		var usernameSelector = $("#usernameSelector1").val();
		var passwordSelector = $("#passwordSelector1").val();
		var submitSelector = $("#submitSelector1").val();
		var autostart = $("#autostart1").is(":checked");
		var yscroll = $("#yscroll").val();
		var zoomLevel = $("#zoom-level").val();
		var timestamp = $("#timestamp1").is(":checked");

		let errorMessages = [];

		if(url.trim() === "") {
			errorMessages.push("URL is required");
		}

		if(loginUrl.trim() === "") {
			errorMessages.push("Login URL is required");
		}

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
			};

			if(username.trim() === "") {
				errorMessages.push("Username is required")
			}

			if(password.trim() === "") {
				errorMessages.push("Password is required")
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
			};

			if(username.trim() === "") {
				errorMessages.push("Username is required");
			}

			if(password.trim() === "") {
				errorMessages.push("Password is required");
			}

			if(usernameSelector.trim() === "") {
				errorMessages.push("Username-Selector is required");
			}

			if(passwordSelector.trim() === "") {
				errorMessages.push("Password-Selector is required");
			}

			if(submitSelector.trim() === "") {
				errorMessages.push("Submit-Selector is required");
			}
		}

		var obj = {
			"name": name,
			"url": url,
			"loginUrl": loginUrl,
			"intervalSeconds": interval,
			"yScroll": yscroll,
			"autostart": autostart,
			"timestamp": timestamp,
			"zoomLevel": zoomLevel,
			authenticationInformation: authenticationInformation
		};

		let failed = $("#save-failed");
		if(errorMessages.length > 0) {
			let s = errorMessages.join("<br />");
			failed.html(s);
			failed.show();
			return;
		}

		failed.hide();
		failed.html("");

		$("#newModal").modal('hide');

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
	});

	$("#authType1").change(function () {
		var $this = $(this);
		var val = $this.val();
		console.log(val);
		if(val === "NONE") {
			$("#username-container").hide();
			$("#password-container").hide();
			$("#username-selector-container").hide();
			$("#password-selector-container").hide();
			$("#submit-selector-container").hide();
		} else if (val === "BASIC") {
			$("#username-container").show();
			$("#password-container").show();
			$("#username-selector-container").hide();
			$("#password-selector-container").hide();
			$("#submit-selector-container").hide();
		} else if (val === "FORM") {
			$("#username-container").show();
			$("#password-container").show();
			$("#username-selector-container").show();
			$("#password-selector-container").show();
			$("#submit-selector-container").show();
		}
	})
});