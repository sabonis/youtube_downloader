inputUrl = document.getElementById('url')
btnSend = document.getElementById('btn_send')
inputGroup = document.getElementById('input_group')
progress = document.getElementsByClassName('progress')[0]

btnSend.onclick = ->
  inputGroup.className += ' hide'
  progress.className += ' show'
  req = new XMLHttpRequest()
  req.open("POST", "/hello");
  req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  req.send(JSON.stringify(url: inputUrl.value));
  req.onload = -> 
    console.log 'server responded.'
    window.open '/files/' + req.response
