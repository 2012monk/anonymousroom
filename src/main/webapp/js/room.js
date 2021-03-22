let msg = {
    roomId : null,
    senderId : null,
    msg : null,
    type : null,
    roomName :null
};
const msgInput = document.getElementById("text-input");
const ul = document.querySelector("#msg-area ul");
document.querySelector("form").addEventListener("submit", send);
const userList = document.getElementById('user-list');

const params = new URLSearchParams(window.location.search);
const roomName = params.get("roomname")
msg.roomName = roomName;
document.getElementById("room-name").innerText = roomName;
document.querySelector('title').innerText = roomName;



const _url = "ws://localhost:47788/ano/"+roomName;
const _URL = "http://localhost:47788/main/"
const socket = new WebSocket(_url);


function send(e) {
    e.preventDefault();
    const text = msgInput.value;
    msg.type = "MSG";
    msg.msg = text;
    msg.senderId = null;

    socket.send(JSON.stringify(msg));
    console.log(msg);
    msgInput.value = "";
}

first = 0;
socket.onmessage = function (e) {
    const response = JSON.parse(e.data);
    const li = document.createElement('li');
    if (first === 0){
        msg.roomId = response.roomId;
        updateUserList();
    }
    first++;


    // li.innerText += (response.id).substr(0, 10);
    li.innerText += response.senderId.split("-")[0];
    li.innerText += ":"
    li.innerText += response.msg;
    ul.appendChild(li);
    console.log(response);
}


socket.onopen = (e) => {
    msg.type = 'ENTER';
    socket.send(JSON.stringify(msg));

}

socket.onclose = (e) => {
    const li = document.createElement('li');
    li.innerText = 'disconnected from server';
    ul.appendChild(li);
}

socket.onError = function (e) {
    console.log(e);
}

function updateUserList(e) {
    const rq = new XMLHttpRequest();
    rq.onreadystatechange = function (e) {
        if (rq.readyState === XMLHttpRequest.DONE){
            if (rq.status === 200){
                const data = JSON.parse(rq.response);

                while (userList.firstChild){
                    userList.removeChild(userList.firstChild);
                }
                const li = document.createElement('li');
                li.innerText = `현재 접속중인 유저 : ${data.length}명`
                userList.appendChild(li);
                for (let i=0; i < data.length;i++){
                    const li = document.createElement('li');
                    const str = JSON.stringify(data[i]);
                    li.innerText =str.split("-")[0].replace( "\"","");
                    userList.appendChild(li);
                }
            }
        }
    }

    rq.open('GET',_URL+`userList?roomId=${roomName}`);
    rq.send()
}


window.setInterval(updateUserList, 4000);






