
const ballrange = document.getElementById("ballrange");
ballrange.addEventListener('mousemove',updateBallRangeIndex)
ballrange.addEventListener('change',updateBallRangeIndex)

const timeRange = document.getElementById("timeRange");
timeRange.addEventListener('change',updateTimeRangeIndex)
timeRange.addEventListener('mousemove',updateTimeRangeIndex)

const ballsColor = document.getElementsByClassName("colorButton");
for (const element of ballsColor) {
    element.addEventListener('change',colorNavigation)
}

const numOfMonsters = document.getElementById("numOfMonsters");
numOfMonsters.addEventListener('change',updatenumOfMonstersIndex)
numOfMonsters.addEventListener('mousemove',updatenumOfMonstersIndex)

const  keys = document.getElementsByClassName("keys");
function updateBallRangeIndex(){
    const ballrangeInx = document.getElementById("ballrangeIndex");
    ballrangeInx.innerText = ballrange.value;
    localStorage.setItem('ballAmount',ballrange.value);
}

function updateTimeRangeIndex(){
    const gametimeIndex = document.getElementById("timeRangeIndex");
    gametimeIndex.innerText = timeRange.value;
    localStorage.setItem('gameTime',timeRange.value);
}

function updatenumOfMonstersIndex(){
    const monsterAmount = document.getElementById("numOfMonstersIndex");
    monsterAmount.innerText = numOfMonsters.value;
    localStorage.setItem('monsterAmount',numOfMonsters.value);

}

function colorNavigation(){
   window.localStorage.setItem(this.id,this.value);
   localStorage.setItem('color1',ballsColor[0].value);
   localStorage.setItem('color2',ballsColor[1].value);
   localStorage.setItem('color3',ballsColor[2].value);
}

function updateKey(event,keyMove){
    keyString = String(keyMove) + "KeyCode"
    const key = document.getElementById(keyString);
    if(event === undefined){
        return;
    }
    key.innerText = event.keyCode;
    localStorage.setItem('up',$('#upKeyCode')[0].innerText);
    localStorage.setItem('down',$('#downKeyCode')[0].innerText);
    localStorage.setItem('left',$('#leftKeyCode')[0].innerText);
    localStorage.setItem('right',$('#rightKeyCode')[0].innerText);
}

function updateRandom(){
    const fivePoints = document.getElementById("fivepoints");
    const fifteenPoints = document.getElementById("fifteenpoints");
    const twentyfivePoints = document.getElementById("twentyfivepoints");
    const upArrow = document.getElementById("upKeyCode");
    const downArrow = document.getElementById("downKeyCode");
    const leftArrow = document.getElementById("leftKeyCode");
    const rightArrow = document.getElementById("rightKeyCode");

    ballrange.value = Math.floor(Math.random() * 41) + 50 ;
    updateBallRangeIndex()
    timeRange.value = Math.floor(Math.random() * 121) + 60 ;
    updateTimeRangeIndex()
    numOfMonsters.value = Math.floor(Math.random() * 4) + 1;
    updatenumOfMonstersIndex()


    fivePoints.value = getRandomColor();
    fifteenPoints.value = getRandomColor();
    twentyfivePoints.value = getRandomColor();

    upArrow.innerText = 38;
    downArrow.innerText = 40;
    rightArrow.innerText = 39;
    leftArrow.innerText = 37;

    localStorage.setItem('color1',ballsColor[0].value);
    localStorage.setItem('color2',ballsColor[1].value);
    localStorage.setItem('color3',ballsColor[2].value);
    localStorage.setItem('up','38');
    localStorage.setItem('down','40');
    localStorage.setItem('right','39');
    localStorage.setItem('left','37');

}

function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
