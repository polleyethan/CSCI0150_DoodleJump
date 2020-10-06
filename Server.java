package DoodleJump;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Creates A Server to handle online multiplayer inputs
 *
 */
public class Server {
	private MultiPlayerGame _game;
	private MultiPlayerOptions _menu;
	private HttpServer _server;

	/**
	 * Constructor for Server. Starts the server
	 */
	public Server(MultiPlayerGame game, MultiPlayerOptions optionsmenu) throws IOException {
		_game = game;
		_menu = optionsmenu;
		this.startServer();
	}

	/**
	 * Starts the Server. Throws IOException for errors with data streaming in the
	 * request and response;
	 */
	public void startServer() throws IOException {
		// Creates HTTPServer
		_server = HttpServer.create(new InetSocketAddress(8500), 0);
		System.out.println(_server);
		// Gets the local IP Address
		InetAddress inetAddress = InetAddress.getLocalHost();
		System.out.println(inetAddress);

		// Sets up the menu showing instructions on how to connect
		_menu.setupCenter(inetAddress.getHostAddress() + ":" + _server.getAddress().getPort());

		// Creates a context for a "/" request: returns a webpage with controls for user
		HttpContext context = _server.createContext("/");

		// Creates a context for "/change" request: updates the game based on the body
		// of the request.
		HttpContext context2 = _server.createContext("/change");

		// Adds contexts to the Server
		context.setHandler(Server::handleRequest);
		context2.setHandler(new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				updateGame(exchange);
			}
		});
		// Start the server
		_server.start();
	}

	/**
	 * Returns a webpage which controls the users game inputs. JavaScript
	 * documentation for the code is as follows: When page is returned, shows an
	 * input textbox and a submit button. On click of the submit button, javascript
	 * sends a post request to the "/change" endpoint, which should return the
	 * players id. Then, the displayed div changes to show two buttons, which
	 * control the direction of the players movement. When the user presses down on
	 * a button, javascript sends a post request to the "/change" endpoint with a
	 * request body that has the players id and the direction of movement. When they
	 * stop pressing the button, javascript sends a post request to the "/change"
	 * endpoint which has a request body that tells the user to stop moving and
	 * includes the players id
	 */
	private static void handleRequest(HttpExchange exchange) throws IOException {
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		responseHeaders.set("Access-Control-Allow-Methods", "GET,POST");
		responseHeaders.set("Access-Control-Allow-Headers",
				"X-Requested-With,Content-Type,Cache-Control,Origin,Accept,Authorization");
		String response = "<!DOCTYPE html>\n" + "<html>\n" + "    <head>\n" + "        <title>Doodle Jump</title>\n"
				+ "            <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n"
				+ "    </head>\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0\" />\n"
				+ "        <style> \n" + "                html{\n" + "                    height: 100%;\n"
				+ "                    text-align: center;\n" + "                }\n" + "                #left{ \n"
				+ "                    float:left;  \n" + "                    background:#EA2027; \n"
				+ "                    width:50%; \n" + "                    height:100%; \n"
				+ "                    text-align: center;\n" + "                    vertical-align:middle;\n"
				+ "                    \n" + "                } \n" + "                #right{ \n"
				+ "                    float:right; \n" + "                    background:#EA2027; \n"
				+ "                    width:50%; \n" + "                    height:100%; \n"
				+ "                    text-align: center;\n" + "                    vertical-align:middle;\n"
				+ "                } \n" + "                body{\n" + "                    overflow:hidden;\n"
				+ "                    width:100%;\n" + "                    height:100%;\n"
				+ "                    margin:0;\n" + "                }\n" + "                #join{\n"
				+ "                    text-align:center;\n" + "                    width:70%;\n"
				+ "                    margin:15%;\n" + "                    margin-left:15%;\n"
				+ "                    \n" + "                }\n" + "                #game{\n"
				+ "                    height:100%;\n" + "                }\n" + "                .textmove{\n"
				+ "                    display:inline-block;\n" + "                    vertical-align:middle;\n"
				+ "                }\n" + "            </style> \n" + "    <body>\n" + "        <div id=\"join\">\n"
				+ "            <form action=\"javascript:void(0);\" onsubmit=\"joinGame();\">\n"
				+ "                    <div class=\"form-group\">\n"
				+ "                        <label for=\"nameBox\">Name:</label>\n"
				+ "                        <input type=\"text\" id=\"nameBox\" class=\"form-control\" aria-describedby=\"nameDesc\" value=\"\">\n"
				+ "                        <small id=\"nameDesc\" class=\"form-text text-muted\">Enter your name to join the game.</small>\n"
				+ "                    </br>\n"
				+ "                        <button type=\"submit\" class=\"btn btn-primary\">Join Game</button>\n"
				+ "                    </div>\n" + "        </form> \n" + "        </div>\n" + "\n"
				+ "        <div id=\"game\" style=\"display:none;\">\n"
				+ "             <div id='left' onmousedown=\"move('left');\" onmouseup=\"stopmove();\" ontouchstart=\"move('left');\" ontouchend=\"stopmove();\"><h1 class=\"display-4 textmove\">Move Left</h1></div>\n"
				+ "             <div id='right' onmousedown=\"move('right');\"ontouchstart=\"move('right');\" onmouseup=\"stopmove();;\" ontouchend=\"stopmove();\"><h1 class=\"display-4 textmove\">Move Right</h1></div>\n"
				+ "        </div>\n" + " <div style=\"display:none;\" id=\"dead\" ><h3>You Died</h3></div>     <script>\n" + "            var pid; \n"
				+ "            function joinGame(){\n"
				+ "                var pname = document.getElementById('nameBox').value;\n"
				+ "                document.getElementById('join').style.display = 'none';\n"
				+ "                document.getElementById('game').style.display = 'inline';\n"
				+ "                var xhr = new XMLHttpRequest();\n"
				+ "                    xhr.open('POST', window.location.href+'change', true);\n"
				+ "                    xhr.setRequestHeader('Content-Type', 'application/json');\n"
				+ "                    xhr.onreadystatechange = function() { // Call a function when the state changes.\n"
				+ "                        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {\n"
				+ "                            console.log(this.response);\n"
				+ "                            pid=JSON.parse(this.response).playerid; console.log(pid);\n"
				+ "                          }\n" + "                    }            \n"
				+ "                    xhr.send(JSON.stringify({\n" + "                        requestType:\"join\",\n"
				+ "                        name: pname\n" + "                    }));\n" + "            }\n"
				+ "            function move(dir){\n" + "                console.log(dir);\n"
				+ "                if(dir == 'left'){\n"
				+ "                    document.getElementById('left').style.background = '#2ed573';\n"
				+ "                    var xhr = new XMLHttpRequest();\n"
				+ "                    xhr.open('POST', window.location.href+'change', true);\n"
				+ "                    xhr.setRequestHeader('Content-Type', 'application/json');\n"
				+ "                    xhr.onreadystatechange = function() {\n"
				+ "                        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {\n"
				+ "                            console.log(this.response);\n"
				+ "                            if(!JSON.parse(this.response).isAlive){\n"
				+ "                                document.getElementById('game').style.display = 'none';\n"
				+ "                                document.getElementById('dead').style.display = 'block';	\n"
				+ "                                } \n" + "                            }\n"
				+ "                        }\n" + "                    xhr.send(JSON.stringify({\n"
				+ "                        requestType:\"move\",\n" + "                        move:'left',\n"
				+ "                        playerid: pid\n" + "                    }));\n" + "                }\n"
				+ "                else{\n"
				+ "                    document.getElementById('right').style.background = '#2ed573';\n"
				+ "                    var xhr = new XMLHttpRequest();\n"
				+ "                    xhr.open('POST', window.location.href+'change', true);\n"
				+ "                    xhr.setRequestHeader('Content-Type', 'application/json');\n"
				+ "                    xhr.onreadystatechange = function() {\n"
				+ "                        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {\n"
				+ "                            console.log(this.response);\n"
				+ "                            if(!JSON.parse(this.response).isAlive){\n"
				+ "                                document.getElementById('game').style.display = 'none';\n"
				+ "                                document.getElementById('dead').style.display = 'block';	\n"
				+ "                                } \n" + "                            }\n"
				+ "                        }\n" + "                    xhr.send(JSON.stringify({\n"
				+ "                        requestType:\"move\",\n" + "                        move:'right',\n"
				+ "                        playerid: pid\n" + "                    }));\n" + "                }\n"
				+ "            }\n" + "\n" + "            function stopmove(){\n"
				+ "                var xhr = new XMLHttpRequest();\n"
				+ "                document.getElementById('right').style.background = '#EA2027';\n"
				+ "                document.getElementById('left').style.background = '#EA2027';\n"
				+ "                    xhr.open('POST', window.location.href+'change', true);\n"
				+ "                    xhr.setRequestHeader('Content-Type', 'application/json');\n"
				+ "                    xhr.onreadystatechange = function() {\n"
				+ "                        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {\n"
				+ "                            console.log(this.response);\n"
				+ "                            if(!JSON.parse(this.response).isAlive){\n"
				+ "                                document.getElementById('game').style.display = 'none';\n"
				+ "                                document.getElementById('dead').style.display = 'block';	\n"
				+ "                                } \n" + "                            }\n"
				+ "                        }\n" + "                    xhr.send(JSON.stringify({\n"
				+ "                        requestType:\"move\",\n" + "                        move:'stop',\n"
				+ "                        playerid: pid\n" + "                    }));\n" + "            }\n"
				+ "        \n" + "        </script>\n" + "    </body>\n" + "</html>";

		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	/**
	 * Updates the game. Called when user makes post request to "/change" endpoint. Uses substrings to parse json requests
	 */
	private void updateGame(HttpExchange exchange) throws IOException {
		String response = "{}";

		// Get body of request
		InputStream s = exchange.getRequestBody();

		InputStreamReader streamreader =  new InputStreamReader(s,"utf-8");
		BufferedReader br = new BufferedReader(streamreader);
		int b;
		StringBuilder buf = new StringBuilder(512);
		while ((b = br.read()) != -1) {
			buf.append((char) b);
		}

		String in = buf.toString();

		//Because no native JSON Parser in Java, using substrings to get the correct values
		int rtStart = in.indexOf("requestType")+14;
		int rtEnd = in.indexOf('"', rtStart);

		String rt = in.substring(rtStart,rtEnd).trim();
		// If the body's "requestType" value == move:
		if (rt.equals("move")) {
			if(in.contains("playerid")) {



				//Because no native JSON Parser in Java, using substrings to get the correct values
				int moveStart = in.indexOf("move",rtEnd)+7;
				int moveEnd = in.indexOf('"',moveStart);
				int playeridStart =in.indexOf("playerid", moveEnd)+10;
				int playeridEnd = in.indexOf('}',playeridStart);

				// Get the direction of moving, or if stop moving
				String move = in.substring(moveStart,moveEnd).replaceAll("'", "").trim();


				// Get the player object from the request body's "playerid" value.
				String playerid = in.substring(playeridStart, playeridEnd).trim();
				Player player = _game.getPlayerById(Integer.parseInt(playerid));

				response = "{\"responseType\": \"gameUpdate\", \"isAlive\":"+player.isAlive()+"}";

				switch (move) {
				case "right": {
					// if the request body's "move" value == "right", start moving the player right.
					_game.movePlayerRight(player);
					break;
				}
				case "left": {
					// if the request body's "move" value == "left", start moving the player left.
					_game.movePlayerLeft(player);
					break;
				}
				case "stop": {
					// if the request body's "move" value == "stop", stop moving the player in the x
					// direction
					_game.stopPlayerXMovement(player);
					break;
				}

				}
			}else {

			}
		}
		// Otherwise, If the body's "requestType" value == join:
		else if (rt.equals("join")) {
			// Add the player to the game and Respond with the ID of the player, so that
			// Javascript can store it.
			int nameStart = in.indexOf("name",rtEnd)+7;
			int nameEnd = in.indexOf('"',nameStart);
			response = "{\"responseType\": \"join\", \"playerid\":"+_menu.getNewId()+"}";
			_menu.addPlayer(in.substring(nameStart,nameEnd));

		}
		// Return a response to the user
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStream os = exchange.getResponseBody();
		os.write(response.toString().getBytes());
		os.close();

	}

	/**
	 * Accessor for the HttpServer object
	 */
	public HttpServer getServer() {
		return _server;
	}
}
