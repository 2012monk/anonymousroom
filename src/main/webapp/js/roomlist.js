const _url = "http://localhost:47788/1";
const roomListUrl = "http://localhost:47788/main/";
const _URL = "http://localhost:47788/room.html";
const ul = document.querySelector("#room-list .list");
// document.querySelector("form").addEventListener("submit", create);



document.querySelector('.result').addEventListener('click', sendRoom);
document.querySelector('.list').addEventListener('click', sendRoom);

function sendRoom(e) {
    const li = e.target;
    if (li.nodeName !== 'LI') return;
    const form = document.createElement('form');
    const input = document.createElement('input');
    form.method = 'POST';
    form.action = _url;

    input.type = 'text';
    input.name = 'roomname';
    input.value = li.name;
    form.appendChild(input);
    document.documentElement.appendChild(form);
    form.submit();
}

function ajax() {
    const httpRequest = new XMLHttpRequest();
    if (httpRequest == null){
        console.log("connection failed")
    }
    httpRequest.onreadystatechange = function () {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                const list = JSON.parse(httpRequest.response);

                while (ul.firstChild){
                    ul.removeChild(ul.firstChild);
                }

                for (let s=0;s<list.length;s++){
                // for (let s in list) {
                    console.log(list);
                    const li = document.createElement("li");
                    const span = document.createElement('span');
                    span.innerText = `현재 인원 : ${JSON.stringify(list[s][1]).replaceAll("\"","")}`
                    li.innerText = `방 제목 : ${JSON.stringify(list[s][0]).replaceAll("\"","")}`;
                    // li.name = s;
                    li.appendChild(span);
                    ul.appendChild(li);
                }
                // }
            } else {
                console.log(httpRequest.status);
            }
        }
    }

    httpRequest.open("GET", roomListUrl+'roomList');
    httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=utf-8');
    httpRequest.send();

}




function open(ev) {
    const value = ev.target.innerText;
    window.location.href = _url;
}

function create(e) {
    const searchForm = document.querySelector('.create');
    const creatForm = document.querySelector('.search');
    const h = 'hidden';
    if (searchForm.classList.contains(h)) {
     searchForm.classList.remove(h);
     creatForm.classList.add(h);
    }else{
        searchForm.classList.add(h);
        creatForm.classList.remove(h);
    }


}

const searchForm = document.querySelector('.search');
searchForm.addEventListener('submit', search)


function search(e) {
    e.preventDefault();
    const op = document.querySelector(".search-btn");
    const resultUl = document.querySelector('.result');
    const h = 'hidden';
    const input = searchForm.querySelector('.place');
    const val = input.value;
    input.value = '';
    if (op.value === 'search'){
        const list = ul.childNodes;
        for (let i=0; i<list.length; i++){
            if (list[i].firstChild.textContent === val) {
                // ul.firstChild.replaceWith(list[i], ul.firstChild);
                resultUl.appendChild(list[i]);
            }
        }
        ul.classList.add(h);
        resultUl.classList.remove(h);
        op.value = 'list';
    }else{
        while (resultUl.firstChild){
            resultUl.removeChild(resultUl.firstChild);
        }
        ul.classList.remove(h);
        resultUl.classList.add(h)
        op.value = 'search';
    }
}

function init() {
    window.setInterval(ajax, 1000);
    document.getElementById('creat-btn').addEventListener('click', create);
}
ajax();
init();