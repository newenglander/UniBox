var app = {
	debugMode : true,
	url : '/UniBox',
	lastTimeStamp : 0,
	cometSource : '/Communicator/JavaScript',
	dataSource : '/Database',
	authSource : '/Auth',
	initialize : function() {
		app.bind();
		app.login();
		app.listen();
		app.initGameTable();
		app.initRankingTable();
		app.updateGameTable();
		app.updateRankingTable();
		app.gotActiveGame();
		app.updateFormulars();
	},
	updateFormulars : function() {
		var categorySelection = $("#gameTypeInput");
		jQuery.ajax({
			type : "GET",
			url : app.url + app.dataSource,
			data : "request=categories",
			success : function(data) {
				$.each(data, function(index, value) {
					$("#gameTypeInput").append(
							'<option value="' + value.CatID + '">'
									+ value.Gametitle + '</option>');
				});
			},
			error : function(e) {
				app.newsticker("warning",
				"Could not update game categories..!");
			},
			async : false
		});
	},
	listen : function() {
		$('#comet-frame').attr("src", app.url + app.cometSource + '?1').load(
				function() {
					console
							.log("reconnect listener after timeout.. Listened "
									+ Math.floor(Date.now() / 1000)
									- app.lastTimeStamp)
							+ " seconds..";
					app.reconnectDialog();
				});
	},
	reconnectDialog : function() {
		swal({
			title : "Disconnected..",
			text : "You are disconnected from the server..",
			type : "warning",
			showCancelButton : false,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "Try reconnect!",
			closeOnConfirm : false,
		}, function(isConfirm) {
			if (isConfirm) {
				app.reload();
			} else {
				app.redirect("timeout");
			}
		});
	},
	activateMenu : function(target) {
		$("#triggerDashboard").removeClass("btnActive");
		$("#triggerChat").removeClass("btnActive");
		$("#triggerAdmin").removeClass("btnActive");
		switch (target.id) {
		case "triggerDashboard":
			app.hideChat();
			app.hideAdmin();
			$(target).addClass("btnActive");
			break;
		case "triggerChat":
			app.showChat();
			app.hideAdmin();
			$(target).addClass("btnActive");
			break;
		case "triggerAdmin":
			app.showAdmin();
			app.hideChat();
			$(target).addClass("btnActive");
			break;
		default:
			break;
		}
		if (window.innerWidth < 768) {
			$(".navbar-toggle").click();
		}
	},
	hideChat : function() {
		$(".messengerFrame").addClass("out");
		setTimeout(function() {
			$(".messengerFrame").addClass("hidden");
		}, 200);
		$("body").removeClass("no-scroll");
	},
	showChat : function() {
		$(".messengerFrame").removeClass("out");
		$(".messengerFrame").removeClass("hidden");
		$("body").addClass("no-scroll");
		setTimeout(function() {
			$("#messengerInputForm").focus();
		}, 100);
	},
	hideAdmin : function() {
		$(".adminFrame").addClass("out");
		setTimeout(function() {
			$(".adminFrame").addClass("hidden");
		}, 200);
		$("body").removeClass("no-scroll");
	},
	showAdmin : function() {
		$(".adminFrame").removeClass("out");
		$(".adminFrame").removeClass("hidden");
		$("body").addClass("no-scroll");
	},
	bind : function() {
		$('#messengerInputForm').keypress(function(e) {
			if (e.which == 13) {
				var formContent = $('#messengerInputForm');
				app.post(formContent.val());
				formContent.val("");
				e.preventDefault();
			}
		});
		$('#refreshOption').click(function() {
			app.reload();
		});
		$('#triggerDashboard').click(function(e) {
			app.activateMenu(this);
		});
		$('#triggerChat').click(function() {
			app.activateMenu(this);
		});
		$('#triggerAdmin').click(function() {
			app.activateMenu(this);
		});
		$('#createGameForm').on(
				'submit',
				function(e) {
					e.preventDefault();
					e.stopPropagation();
					var gameName = $('#gameNameInput').val();
					var gameType = $('#gameTypeInput').val();
					var gameDescription = $('#gameDescriptionInput').val();
					jQuery.ajax({
						type : "POST",
						url : app.url + app.dataSource,
						data : "create=game&gamename=" + gameName + "&catid="
								+ gameType + "&descr=" + gameDescription,
						success : function(result) {
							app.newsticker("success",
									"Game created successful.");
							app.updateGameTable();
						},
						error : function(e) {
							app.newsticker("warning",
									"Could not create game..!");
						},
						async : false
					});
				});
		$('#changePasswordForm')
				.on(
						'submit',
						function(e) {
							e.preventDefault();
							e.stopPropagation();
							var oldPassword = $('#oldPassword').val();
							var inputPassword = $('#inputPassword').val();
							var inputPasswordConfirm = $(
									'#inputPasswordConfirm').val();
							jQuery
									.ajax({
										type : "POST",
										url : app.url + app.authSource,
										data : "action=changePassword&oldPassword="
												+ Base64.encode(oldPassword)
												+ "&inputPassword="
												+ Base64.encode(inputPassword)
												+ "&inputPasswordConfirm="
												+ Base64
														.encode(inputPasswordConfirm),
										success : function(result) {
											if (result == "success") {
												app.newsticker("success",
														"Password changed.");
											} else {
												app
														.newsticker("warning",
																"Could not change password..!");
											}
										},
										error : function(e) {
											app
													.newsticker("warning",
															"Could not change password..!");
										},
										async : false
									});
						});
		$('#newGameModal').on('shown.bs.modal', function() {
			$('#gameNameInput').focus();
		});
		$('#changePasswordModal').on('shown.bs.modal', function() {
			$('#oldPassword').focus();
		});
		$("#changePasswordForm").validator();
		$(document).on("keydown", function disableF5(e) {
			if ((e.which || e.keyCode) == 116) {
				if (!app.debugMode) {
					e.preventDefault();
					app.reload();
				}
			}
		});
//		$('.navbar-collapse').on('hidden.bs.collapse', function() {
//			console.log("HI");
//			$(".navbar-toggle").blur();
//		});
//		$(".navbar-toggle").focusout(function() {
//			console.log("IH");
//			if ($(".navbar-collapse").hasClass("in")) {
//				$(this).click();
//			}
//		});
		/**
		 * DEMO for status panel
		 * 
		 * $('.alert') .click( function() { // DEMO app.newsticker("info",
		 * "Info: UniBox connects your Java Games.."); setTimeout( function() {
		 * app .newsticker("warning", "Warning: It´s boring.. UniBox is
		 * idling.."); }, 4000); setTimeout( function() { app
		 * .newsticker("danger", "Danger: UniBox is almost fell asleep..
		 * zzZZzz.."); }, 8000); setTimeout( function() { app
		 * .newsticker("success", "Success: ..How are you? UniBox is ready to
		 * conquer!"); }, 12000); });
		 */
	},
	reload : function() {
		swal({
			title : "Reloading..",
			text : "Please wait a second..",
			showConfirmButton : false,
			type : "success",
			timer : "2000"
		});
		app.updateGameTable();
		app.updateRankingTable();
		app.listen();
	},
	login : function() {
		jQuery.ajax({
			type : "POST",
			url : app.url + app.cometSource,
			data : "action=connect",
			success : function(result) {
				app.lastTimeStamp = Math.floor(Date.now() / 1000);
			},
			error : function() {
				app.redirect("login");
			},
			async : false
		});
	},
	post : function(message) {
		if (message) {
			jQuery.ajax({
				type : "POST",
				url : app.url + app.cometSource,
				data : "action=post&message=" + Base64.encode(message),
				error : function() {
					app.redirect("post");
				},
				async : false
			});
		}
	},
	message : function(data) {
		$(
				'<div class="messengerElement"><div><span class="messageText"><span>'
						+ data.name + ':</span> ' + Base64.decode(data.message)
						+ '</span></div></div>').appendTo('#messengerContent')
				.fadeIn('slow');
		scrollBottom($('.messengerContent'));
	},
	notice : function(text) {
		$(
				'<div class="messengerElement"><div><span class="messageText"><span>System:</span> '
						+ text + '</span></div></div>').appendTo(
				'#messengerContent').fadeIn('slow');
		scrollBottom($('.messengerContainer'));
	},
	redirect : function(reason) {
		window.location.replace("/UniBox/?error=" + reason);
	},
	initGameTable : function() {
		var gameTable = $('#gamePanel').dataTable({
			"pagingType" : "simple",
			"iDisplayLength" : 10,
			"bLengthChange" : false,
			"responsive" : true,
			"bFilter" : true,
			"language" : {
				"processing" : "just a second..",
				"lengthMenu" : "Show _MENU_",
				"zeroRecords" : "No data available",
				"info" : "_END_ of _TOTAL_",
				"infoEmpty" : "0 to 0 of 0",
				"infoFiltered" : "out of _MAX_)",
				"infoPostFix" : "",
			},
		});
		$('#gameFilter').keyup(function() {
			$("#gamePanel_filter input").val($(this).val());
			$("#gamePanel_filter input").keyup()
		});
	},
	updateGameTable : function() {
		var gameDataTable = $('#gamePanel').DataTable();
		jQuery.ajax({
			type : "GET",
			url : app.url + app.dataSource + "?request=games",
			success : function(data) {
				gameDataTable.clear().draw();
				for ( var i in data) {
					var gameJoinLink = '<a id="game-' + data[i].GameID
							+ '" href="javascript:app.joinGame('
							+ data[i].GameID + ')">Join</a>';
					gameDataTable.row.add([ gameJoinLink, data[i].GameID,
							data[i].Gametitle, data[i].GameName,
							data[i].NumberOfPlayers ]);
				}
				gameDataTable.draw();
			},
			error : function() {
				app.newsticker("warning", "Could not update game table..!");
			},
			statusCode : {
				403 : function() {
					app.redirect("forbidden");
				},
				0 : function() {
					app.redirect("server_offline");
				}
			},
			async : false
		});
	},
	initRankingTable : function() {
		var rankingTable = $('#rankingPanel').dataTable({
			"pagingType" : "simple",
			"iDisplayLength" : 10,
			"bLengthChange" : false,
			"responsive" : true,
			"bFilter" : true,
			"language" : {
				"processing" : "just a second..",
				"lengthMenu" : "Show _MENU_",
				"zeroRecords" : "No data available",
				"info" : "_END_ of _TOTAL_",
				"infoEmpty" : "0 of 0",
				"infoFiltered" : "out of _MAX_)",
				"infoPostFix" : "",
			},
		});
		$('#rankingFilter').keyup(function() {
			$("#rankingPanel_filter input").val($(this).val());
			$("#rankingPanel_filter input").keyup()
		});
	},
	updateRankingTable : function() {
		var rankingDataTable = $('#rankingPanel').DataTable();
		jQuery.ajax({
			type : "GET",
			url : app.url + app.dataSource + "?request=ranking",
			success : function(data) {
				rankingDataTable.clear().draw();
				for ( var i in data) {
					rankingDataTable.row.add([ data[i].Rank, data[i].Name,
							data[i].Score ]);
				}
				rankingDataTable.draw();
			},
			error : function() {
				app.newsticker("warning", "Could not update ranking table..!");
			},
			statusCode : {
				403 : function() {
					app.redirect("forbidden");
				},
				0 : function() {
					app.redirect("server_offline");
				}
			},
			async : false
		});
	},
	newsticker : function(type, message) {
		switch (type) {
		case "info":
			app.promoteNews(type, message);
			break;
		case "warning":
			app.promoteNews(type, message);
			break;
		case "success":
			app.promoteNews(type, message);
			break;
		case "danger":
			app.promoteNews(type, message);
			break;
		default:
			break;
		}
	},
	promoteNews : function(type, message) {
		$(".newsTicker").empty();
		$(".newsTicker").append(
				'<div class="alert alert-' + type + '" role="alert">' + message
						+ '</div>');
	},
	joinGame : function(id) {
		jQuery.ajax({
			type : "GET",
			url : app.url + "/Game?action=join&gameid=" + id,
			success : function(data) {
				app.updateGameTable();
				var linkElement = $("#game-" + id);
				linkElement
						.attr("href", "javascript:app.leaveGame(" + id + ")");
				linkElement.text("leave");
			},
			error : function() {
				app.newsticker("warning", "Could not rejoin game..!");
			},
			statusCode : {
				403 : function() {
					app.redirect("forbidden");
				},
				0 : function() {
					app.redirect("server_offline");
				}
			},
			async : false
		});
	},
	leaveGame : function(id) {
		jQuery
				.ajax({
					type : "GET",
					url : app.url + "/Game?action=leave&gameid=" + id,
					success : function(data) {
						app.updateGameTable();
						var linkElement = $("#game-" + id);
						linkElement.attr("href", "javascript:app.joinGame("
								+ id + ")");
						linkElement.text("join");
					},
					error : function() {
						app.newsticker("warning", "Could not releave game..!");
					},
					statusCode : {
						403 : function() {
							app.redirect("forbidden");
						},
						0 : function() {
							app.redirect("server_offline");
						}
					},
					async : false
				});
	},
	gotActiveGame : function() {
		jQuery.ajax({
			type : "GET",
			url : app.url + "/Game?action=whichgame",
			success : function(data) {
				var id = data.replace("gameid:", "");
				var linkElement = $("#game-" + id);
				linkElement
						.attr("href", "javascript:app.leaveGame(" + id + ")");
				linkElement.text("leave");
			},
			statusCode : {
				403 : function() {
					app.redirect("forbidden");
				},
				0 : function() {
					app.redirect("server_offline");
				}
			},
			async : false
		});
	}
};