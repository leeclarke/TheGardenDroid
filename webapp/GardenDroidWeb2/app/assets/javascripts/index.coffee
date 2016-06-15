$ ->

  # Server Sent Events
  $("#sse-demo").click () ->
    events = new EventSource("/events")
    events.onmessage = (e) ->
      alert(e.data)
      # close the connection so that we can do this again (normally you wouldn't do this)
      events.close()

  # WebSocket
  ws = new WebSocket("ws://localhost:9000/echo")
  ws.onmessage = (message) ->
    alert(message.data)
  $("#websocket-demo").click () ->
    ws.send("foo")