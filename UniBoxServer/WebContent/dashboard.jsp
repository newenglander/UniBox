<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="admin" class="de.unibox.http.servlet.beans.AdminBean"
	scope="session" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>UniBox</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, user-scalable=0" />
<meta name="description" content="">
<meta name="author" content="">

<link href="css/vendor/bootstrap.min.css" rel="stylesheet"
	type="text/css">
<link href="css/vendor/jquery.dataTables.min.css" rel="stylesheet"
	type="text/css">
<link href="css/vendor/dataTables.responsive.css" rel="stylesheet"
	type="text/css">
<link href="css/vendor/dataTables.bootstrap.css" rel="stylesheet"
	type="text/css">
<link href="css/vendor/sweet-alert.css" rel="stylesheet">
<link href="css/main.css" rel="stylesheet" type="text/css">

<!--[if lt IE 9]>
      <script src="js/vendor/html5shiv.min.js" type="text/javascript"></script>
<![endif]-->

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<body>
	<nav class="navbar navbar-fixed-top navbar-inverse" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand noMargin" href="/UniBox/Dashboard">Uni <span
					class="logoBox">Box</span></a>
			</div>
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
					<li><a class="whiteText btnActive" id="triggerDashboard"
						href="#">Dashboard</a></li>
					<li><a class="whiteText" id="triggerChat" href="#">Chat</a></li>
					<jsp:getProperty name="admin" property="adminMenu" />
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">Connection <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a id="refreshOption" href="">Reconnect Chat</a></li>
							<li><a href="Dashboard">Reload Page</a></li>
						</ul></li>
					<li><a class="whiteText" href="/UniBox/Logout">Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="jumbotron infoMessage">
		<div class="container">
			<div class="row">
				<div class="col-xs-12 col-sm-7 col-md-7 col-lg-7 newsTicker">
					<div class="alert alert-info" role="alert">UniBox -
						Initializing Newsticker..</div>
				</div>
				<div class="col-xs-12 col-sm-5 col-md-5 col-lg-5">
					<div class="list-group">
						<div class="list-group-item list-group-item-menu padding15"
							data-toggle="modal" data-target=".bs-new-game-modal-lg">Create
							new Game</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
				<div class="row">
					<div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="panel panel-primary panel-default gamePanel">
									<div class="panel-heading">
										<div class="row">
											<div class="col-xs-6">
												<h3 class="panel-title">Game Panel</h3>
											</div>
											<div class="col-xs-6">
												<input type="search" class="form-control input-sm"
													id="gameFilter" placeholder="Filter.."
													aria-controls="gamePanel">
											</div>
										</div>
									</div>
									<div class="panel-body">
										<div class="table-responsive">
											<table id="gamePanel"
												class="table table-striped table-bordered table-hover dt-responsive display nowrap">
												<thead>
													<tr>
														<th></th>
														<th>ID</th>
														<th>Game</th>
														<th>Name</th>
														<th>Places</th>
													</tr>
												</thead>
												<tbody id="gameTableBody">
												</tbody>
												<tfoot>
													<tr>
														<th></th>
														<th>ID</th>
														<th>Game</th>
														<th>Name</th>
														<th>Places</th>
													</tr>
												</tfoot>
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-xs-12 col-sm-5 col-md-5 col-lg-5">
						<div class="row">
							<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="panel panel-primary panel-default rankingPanel">
									<div class="panel-heading">
										<div class="row">
											<div class="col-xs-6">
												<h3 class="panel-title">Ranking Panel</h3>
											</div>
											<div class="col-xs-6">
												<input type="search" class="form-control input-sm"
													id="rankingFilter" placeholder="Filter.."
													aria-controls="rankingPanel">
											</div>
										</div>
									</div>
									<div class="panel-body">
										<div class="table-responsive">
											<table id="rankingPanel"
												class="table table-striped table-bordered table-hover dt-responsive display nowrap">
												<thead>
													<tr>
														<th>Rank</th>
														<th>Player</th>
														<th>Score</th>
													</tr>
												</thead>
												<tbody id="rankingTableBody">
												</tbody>
												<tfoot>
													<tr>
														<th>Rank</th>
														<th>Player</th>
														<th>Score</th>
													</tr>
												</tfoot>
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="messengerFrame out hidden">
		<div class="messengerContainer">
			<div class="messengerContent bottom" id="messengerContent">
				<div class="messengerElement">
					<div>
						<span class="messageText"><span>System:</span> Willkommen
							im UniBox Chat..</span>
					</div>
				</div>
			</div>
			<div class="messengerInput">
				<input id="messengerInputForm" type="text" placeholder="Message.."
					class="form-control expandMax">
			</div>
		</div>
	</div>

	<div class="adminFrame out hidden">
		<div class="adminContainer">
			<div class="container">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
						Admin Options here..</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade bs-new-game-modal-lg" id="newGameModal"
		tabindex="-1" role="dialog" aria-labelledby="Create new Game"
		aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content modalFormContent">
				<form class="form-horizontal">
					<fieldset>
						<legend>Create new Game</legend>
						<div class="form-group">
							<label class="col-md-4 control-label" for="gameName">Gamename</label>
							<div class="col-md-4">
								<input id="gameNameInput" name="gameName" type="text"
									placeholder="Gamename" class="form-control input-md" required>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label" for="gameType">Select
								Gametype</label>
							<div class="col-md-4">
								<select id="gameTypeInput" name="gameType" class="form-control">
									<option value="1">TicTacToe</option>
									<option value="2">Vier Gewinnt</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label" for="Description">Description</label>
							<div class="col-md-4">
								<textarea class="form-control" id="descriptionGameInput"
									name="Description">Description..</textarea>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-8 floatRight">
								<button id="cancelNewGame" name="cancelNewGame"
									class="btn btn-danger" type="button" data-toggle="modal"
									data-target=".bs-new-game-modal-lg">Cancel</button>
								<button id="commitNewGame" name="commitNewGame"
									class="btn btn-success" type="submit">Commit</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>

	<iframe id="comet-frame" style="display: none;"></iframe>

	<script src="js/vendor/jquery.min.js" type="text/javascript"></script>
	<script src="js/vendor/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/vendor/jquery.dataTables.min.js" type="text/javascript"></script>
	<script src="js/vendor/dataTables.responsive.min.js"
		type="text/javascript"></script>
	<script src="js/vendor/dataTables.bootstrap.js" type="text/javascript"></script>
	<script src="js/vendor/sweet-alert.min.js" type="text/javascript"></script>
	<script src="js/vendor/base64.js"></script>
	<script src="js/main.js" type="text/javascript"></script>
	<script src="js/app.js" type="text/javascript"></script>

	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="js/vendor/ie10-viewport-bug-workaround.js"></script>

</body>
</html>