var context = canvas.getContext("2d");

var shape=new Object();
var monster1Shape = new Object();
var monster2Shape = new Object();
var monster3Shape = new Object();
var monster4Shape = new Object();
var pointsShape = new Object();

var board;
var score;
var pac_color;
var emptyCells;

var start_time;
var time_elapsed; 

var interval;
var monsterInterval;
var shapeInterval;

var pacman_eye1 = 5;
var pacman_eye2 = -15;
var pacman_body1 = 0.15;
var pacman_body2 = 1.85;
var audio ;
var win_audio;
var lose_audio;
var five_amount;
var fifteen_amount;
var twentyfive_amount;

var monsterStrikes;

var scoreLabel = $("#currentScore");


function Start() {
    audio = new Audio("themePacman.mp3");
    audio.loop=true;
    win_audio=new Audio("pacmanWin.mp3");
    lose_audio=new Audio("pacmanLose.mp3");
    audio.play();
    emptyCells = new Array();
    board = new Array();
    monsterStrikes = 0;
    var lifeAmount = 5;
    lifeAmount=5;
    score = 0;
    pac_color="yellow";
    var cnt = 100;
    var monster_counter = 10;
    var food_remain = localStorage.getItem("ballAmount");
    var five_points = Math.floor(food_remain*0.6);
    var fifteen_points = Math.floor(food_remain*0.3);
    var twentyfive_points = Math.floor(food_remain*0.1);
    lblLife.value=5;
    lblLife.innerText = 5;
    lblUser.value = document.getElementById("username").value;
    lblTotalTime.value = localStorage.getItem("gameTime");
    fillInputSetting();
    if(five_points+fifteen_points+twentyfive_points<food_remain){
        five_points=five_points+(food_remain-five_points-fifteen_points-twentyfive_points);
     }
    five_amount = five_points;
    fifteen_amount = fifteen_points;
    twentyfive_amount = twentyfive_points;
    var pacman_remain = 1;
    start_time= new Date();
    var monster_num = localStorage.getItem("monsterAmount");
    for (var i = 0; i < 10; i++) {
        board[i] = new Array();    
        for (var j = 0; j < 10; j++) {
            if((i==3 && j==3)||(i==3 && j==4)||(i==3 && j==5)||(i==6 && j==1)||(i==6 && j==2) || (i==8 && j==9)||(i==7&&j==7)||(i==1&&j==6))
            {
                if(i==8 && j==9){
                    board[i][j] = 3;
                    pointsShape.i = i;
                    pointsShape.j = j;
                }else if(i==7&&j==7){//medicine
                    board[i][j]=20;
                }else if(i==1&&j==6){//clock
                    board[i][j]=21;
                }
                else{
                    board[i][j] = 4;
                }
                continue;
            }
            if(((i==0&&j==0)||(i==0&&j==9)||(i==9&&j==0)||(i==9&&j==9))&&monster_num>0){
                board[i][j]=monster_counter;
                if(monster_counter == 10){
                    monster1Shape.i = i;
                    monster1Shape.j = j;
                }else if(monster_counter ==11){
                    monster2Shape.i = i;
                    monster2Shape.j = j;
                }else if(monster_counter ==12){
                    monster3Shape.i = i;
                    monster3Shape.j = j;
                }else{
                    monster4Shape.i = i;
                    monster4Shape.j = j;
                }
                monster_counter++;
                monster_num--;

            }
            else{
                var randomNum = Math.random();
                if (randomNum <= 1.0 * five_points / cnt) {
                    five_points--;
                    board[i][j] = 5;
                } else if (randomNum <= 1.0 * fifteen_points / cnt){
                    fifteen_points--;
                    board[i][j] = 6

                }else if(randomNum <= 1.0 * twentyfive_points / cnt){
                    twentyfive_points--;
                    board[i][j] = 7
                }
                else if (randomNum < 1.0 * (pacman_remain + food_remain) / cnt && pacman_remain > 0) {
                    shape.i=i;
                    shape.j=j;
                    pacman_remain--;
                    board[i][j] = 2;
                } else {
                    emptyCells.push({row:i,col:j})
                    board[i][j] = 0;
                }
                cnt--;
            }
        }
    }
    fillRemaining(5,five_points);
    fillRemaining(6,fifteen_points);
    fillRemaining(7,twentyfive_points);
    keysDown = {};
    addEventListener("keydown", function (e) {
        keysDown[e.keyCode] = true;
        e.preventDefault();
    }, false);
    addEventListener("keyup", function (e) {
        keysDown[e.keyCode] = false;
        e.preventDefault();
    }, false);
    interval=setInterval(UpdatePosition, 200);
    shapeInterval = setInterval(updatePointsShapeMove,600);
    monsterInterval = setInterval(function(){
        for(var i=0;i<localStorage.getItem("monsterAmount");i++){
            if(i == 0){
                updateMonsterMove(monster1Shape,shape.i,shape.j,10)
            }else if(i==1){
                updateMonsterMove(monster2Shape,shape.i,shape.j,11)
            }else if(i==2){
                updateMonsterMove(monster3Shape,shape.i,shape.j,12)
            }else{
                updateMonsterMove(monster4Shape,shape.i,shape.j,13)
            }
        }
    },600);
    
}


function fillInputSetting(){
    var up_value = localStorage.getItem("up");
    var down_value = localStorage.getItem("down");
    var left_value = localStorage.getItem("left");
    var right_value = localStorage.getItem("right");
    if(up_value==38){
        lblUp.value = "Up Arrow";
    }
    else{
        lblUp.value = String.fromCharCode(localStorage.getItem("up"));
    }
    if(down_value==40){
        lblDown.value = "Down Arrow"
    }
    else{
        lblDown.value = String.fromCharCode(localStorage.getItem("down"));
    }
    if(right_value==39){
        lblRight.value = "Right Arrow"
    }else{
        lblRight.value = String.fromCharCode(localStorage.getItem("right"));
    }
    if(left_value==37){
        lblLeft.value = "Left Arrow"
    }
    else{
        lblLeft.value = String.fromCharCode(localStorage.getItem("left"));
    }
}

function fillRemaining(type,remainingAmount){

    while (remainingAmount > 0){
        var emptyCell = findRandomEmptyCell()
        board[emptyCell[0]][emptyCell[1]] = type;
        remainingAmount--;
    }

}



function findRandomEmptyCell(){
    var randomIndex = emptyCells[Math.floor(Math.random()*emptyCells.length)]
    var cell = emptyCells.splice(randomIndex,1)[0]
    return [cell.row,cell.col]

}

function GetKeyPressed() {
    var up = localStorage.getItem("up");
    if (keysDown[parseInt(localStorage.getItem("up"))]) {
        pacman_body1 = -0.35;
		pacman_body2 = 1.35;
		pacman_eye1 = -15;
		pacman_eye2 = 5;
        return 1;
    }
    if (keysDown[parseInt(localStorage.getItem("down"))]) { 
        pacman_body1 = 0.65;
		pacman_body2 = 2.35;
		pacman_eye1 = -15;
		pacman_eye2 = 5;
        return 2;
    }
    if (keysDown[parseInt(localStorage.getItem("left"))]) { 
        pacman_body1 = 1.15
		pacman_body2 = 2.85;
		pacman_eye1 = 5;
		pacman_eye2 = -15;
        return 3;
    }
    if (keysDown[parseInt(localStorage.getItem("right"))]) { 
        pacman_body1 = 0.15;
		pacman_body2 = 1.85;
		pacman_eye1 = 5;
		pacman_eye2 = -15;
        return 4;
    }
}

function Draw() {
    const fivePointsColor = localStorage.getItem("color1");
    const fifteenPointsColor = localStorage.getItem("color2");
    const twentyFivePointsColor = localStorage.getItem("color3");
    canvas.width=canvas.width; //clean board
    lblScore.value = score;
    lblTime.value = time_elapsed;
    for (var i = 0; i < 10; i++) {
        for (var j = 0; j < 10; j++) {
            var center = new Object();
            center.x = i * 60 + 30;
            center.y = j * 60 + 30;
            if((typeof pointsShape !=("undefined")) && pointsShape.i==i && pointsShape.j==j){ // 50 points shape
                context.beginPath();
                var gap = 0.3; // increase this for spacing between spiral lines        
                var STEPS_PER_ROTATION = 60; // increasing this makes the curve smoother
                var increment = 2*Math.PI/STEPS_PER_ROTATION;		
                var theta = increment;
                while( theta < 20*Math.PI) {
                var newX = center.x + theta * Math.cos(theta) * gap; 
                var newY = center.y + theta * Math.sin(theta) * gap; 
                context.lineTo(newX, newY);
                theta = theta + increment;
                }
                context.strokeStyle= "pink";
                context.stroke();
                continue;
            }
            if (board[i][j] == 2) {//pacman
                context.beginPath();
                context.arc(center.x, center.y, 30, pacman_body1 * Math.PI, pacman_body2 * Math.PI); // half circle
                context.lineTo(center.x, center.y);
                context.fillStyle = pac_color; //color 
                context.fill();
                context.beginPath();
                context.arc(center.x + pacman_eye1, center.y + pacman_eye2, 5, 0, 2 * Math.PI); // circle
                context.fillStyle = "black"; //color 
                context.fill();
            } else if (board[i][j] == 5) {//5point
                context.beginPath();
                context.arc(center.x, center.y, 15, 0, 2 * Math.PI); // circle
                context.fillStyle = fivePointsColor; //color 
                context.fill();
            }
            else if (board[i][j] == 6) {//15point
                context.beginPath();
                context.arc(center.x, center.y, 15, 0, 2 * Math.PI); // circle
                context.fillStyle = fifteenPointsColor; //color 
                context.fill();
            }else if (board[i][j] == 7) {//25point
                context.beginPath();
                context.arc(center.x, center.y, 15, 0, 2 * Math.PI); // circle
                context.fillStyle = twentyFivePointsColor; //color 
                context.fill();
            }
            else if (board[i][j] == 4) {//wall
                context.beginPath();
                context.rect(center.x-30, center.y-30, 60, 60);
                context.fillStyle = "blue"; //color 
                context.fill();
            }
            else if(board[i][j]==20){
                med_img = new Image();
                med_img.src="./image/med.png";
                context.drawImage(med_img,center.x-30,center.y-30,canvas.width/12,canvas.height/12);
            } 
            else if(board[i][j]==21){
                clock_img = new Image();
                clock_img.src="./image/clock.png";
                context.drawImage(clock_img,center.x-30,center.y-30,canvas.width/12,canvas.height/12);
            }
            
            if((typeof monster1Shape !=("undefined"))&&monster1Shape.i==i&&monster1Shape.j==j){
                monster_img1 = new Image();
                monster_img1.src="./image/monster1.png";
                context.drawImage(monster_img1,center.x-30,center.y-30,canvas.width/12,canvas.height/12);
            }
            if((typeof monster2Shape !=("undefined"))&&monster2Shape.i==i&&monster2Shape.j==j){
                monster_img2 = new Image();
                monster_img2.src="./image/monster2.png";
                context.drawImage(monster_img2,center.x-30,center.y-30,canvas.width/12,canvas.height/12);
            }
            if((typeof monster3Shape !=("undefined"))&&monster3Shape.i==i&&monster3Shape.j==j){
                monster_img3 = new Image();
                monster_img3.src="./image/monster3.png";
                context.drawImage(monster_img3,center.x-30,center.y-30,canvas.width/12,canvas.height/12);
            }
            if((typeof monster4Shape !=("undefined"))&&monster4Shape.i==i&&monster4Shape.j==j){
                monster_img4 = new Image();
                monster_img4.src="./image/monster4.png";
                context.drawImage(monster_img4,center.x-30,center.y-30,canvas.width/12,canvas.height/12);
            }
        }
    }

   
}


function UpdatePosition() {
    if(board === undefined){
        return;
    }
    var currentTime=new Date();

    board[shape.i][shape.j]=0;
    updateShapePosition();
    updateScore();
    updateStrikes();
    board[shape.i][shape.j]=2;
    
    time_elapsed=(currentTime-start_time)/1000;
    
    if(score<100 && parseInt(lblTotalTime.value) - parseInt(lblTime.value) <= 0)
    {
        window.clearInterval(interval);
        window.clearInterval(monsterInterval);
        window.clearInterval(shapeInterval);
        paudio.pause();
        lose_audio.play();
        alert("You are better than "+score+" points!")
    }
    else if(score>100 && parseInt(lblTotalTime.value) - parseInt(lblTime.value)  <= 0){
        window.clearInterval(interval);
        window.clearInterval(monsterInterval);
        window.clearInterval(shapeInterval);
        audio.pause();
        win_audio.play();
        alert("Winner!!!")
    }
    else if(score >= (5 * five_amount + 15 * fifteen_amount + 25 * twentyfive_amount))
    {
        window.clearInterval(interval);
        window.clearInterval(monsterInterval);
        window.clearInterval(shapeInterval);
        audio.pause();
        win_audio.play();
        alert("Winner!!!");
    }
    else
    {
        Draw();
    }
}

function restart(){
    audio.pause();
    window.clearInterval(interval);
    window.clearInterval(monsterInterval);
    window.clearInterval(shapeInterval);
    Start();
}

function updateScore(){
    if(board[shape.i][shape.j]==5 ){
        score+=5;
    }else if(board[shape.i][shape.j]==6){
        score+=15
    }else if(board[shape.i][shape.j]==20){
        lblLife.value=parseInt(lblLife.value)+1;
        monsterStrikes--;
    }
    else if(board[shape.i][shape.j]==21){
        lblTotalTime.value=parseInt(lblTotalTime.value)+30;
    }
    else if(board[shape.i][shape.j] == 7){
        score+= 25
    }else if(shape.i == pointsShape.i && shape.j == pointsShape.j){
        score+= 50
        pointsShape.i = -1;
        pointsShape.j = -1;
    }

}

function updateStrikes(){
    var newPosition;
    if((shape.i == monster1Shape.i && shape.j == monster1Shape.j) || 
    (typeof monster2Shape !="undefined" &&shape.i == monster2Shape.i && shape.j == monster2Shape.j)
    || (typeof monster3Shape !="undefined" && shape.i == monster3Shape.i && shape.j == monster3Shape.j)
    ||(typeof monster4Shape !="undefined" && shape.i == monster4Shape.i && shape.j == monster4Shape.j)){
        if(score-10<0){
            score=0;
        }
        else{
            score-=10;
        }  
        monsterStrikes++;
        lblLife.value = 5 - monsterStrikes;   
        if(monsterStrikes >= 5){
            audio.pause();
            lose_audio.play();
            alert("Loser");
            window.clearInterval(interval);
            window.clearInterval(monsterInterval);
            window.clearInterval(shapeInterval);

        }else{
            board[shape.i][shape.j] = 0;
            newPosition = findRandomEmptyCell();
            shape.i = newPosition[0];
            shape.j = newPosition[1];
            updateMonsterStartPosition();
        }             
    }
}

function updateShapePosition(){
    var x = GetKeyPressed()
    if(x==1)
    {
        if(shape.j>0 && board[shape.i][shape.j-1]!=4)
        {
            shape.j--;
        }
    }
    if(x==2)
    {
        if(shape.j<9 && board[shape.i][shape.j+1]!=4)
        {
            shape.j++;
        }
    }
    if(x==3)
    {
        if(shape.i>0 && board[shape.i-1][shape.j]!=4)
        {
            shape.i--;
        }
    }
    if(x==4)
    {
        if(shape.i<9 && board[shape.i+1][shape.j]!=4)
        {
            shape.i++;
        }
    }

}

function updateMonsterStartPosition(){
    for(var i=0;i<localStorage.getItem("monsterAmount");i++){
        if(i == 0){
            board[0][0] = 10
            monster1Shape.i = 0;
            monster1Shape.j = 0;
        }else if(i==1){
            board[0][9] = 10
            monster2Shape.i = 0;
            monster2Shape.j = 9;
        }else if(i==2){
            board[9][0] = 10
            monster3Shape.i = 9;
            monster3Shape.j = 0;
        }else{
            board[9][9] = 10
            monster4Shape.i = 9;
            monster4Shape.j = 9;
        }
    }
}


function updateMonsterMove(monster,i,j,type){

    if(monster === undefined || i == undefined || j==undefined){
        return;
    }
    if(monster.i<i&&board[monster.i+1][monster.j]!=4 && !checkMonsterCollide(type,monster.i + 1,monster.j)){
        monster.i++;
        return;
    }
    if(monster.j<j&&board[monster.i][monster.j+1]!=4&& !checkMonsterCollide(type,monster.i,monster.j+1)){
        monster.j++;
        return;
    }
    if(monster.j>j&&board[monster.i][monster.j-1]!=4&& !checkMonsterCollide(type,monster.i,monster.j-1)){
        monster.j--;
        return;
    }
    if(monster.i>i&&board[monster.i-1][monster.j]!=4&& !checkMonsterCollide(type,monster.i - 1,monster.j)){
        monster.i--;
        return;
    }
}

function checkMonsterCollide(type,i,j){
    for(var k =0;k < localStorage.getItem("monsterAmount");k++){
        if(type == k+10){
            continue;
        }
        if(k == 0){
            if(i == monster1Shape.i && j == monster1Shape.j){
                return true;
            }
        }
        if(k == 1){
            if(i == monster2Shape.i && j == monster2Shape.j){
                return true;
            }
        }
        if(k == 2){
            if(i == monster3Shape.i && j == monster3Shape.j){
                return true;
            }
        }
        if(k == 3){
            if(i == monster4Shape.i && j == monster4Shape.j){
                return true;
            }
        }
    }
    return false;
}

function updatePointsShapeMove(){
    if(pointsShape.i == -1){
        return;
    }
    var randNum = Math.random()
    if(randNum < 0.25){
        if(pointsShape.j>0 && board[pointsShape.i][pointsShape.j-1]!=4){
            pointsShape.j--;
        }else if(pointsShape.i < 9  && board[pointsShape.i+1][pointsShape.j]!=4){
            pointsShape.i++;
        }else if(pointsShape.j < 9  && board[pointsShape.i][pointsShape.j+1]!=4){
            pointsShape.j++;
        }else if(pointsShape.i > 0  && board[pointsShape.i-1][pointsShape.j]!=4){
            pointsShape.i--;
        }
    }else if(randNum < 0.5){
        if(pointsShape.i < 9  && board[pointsShape.i+1][pointsShape.j]!=4){
            pointsShape.i++;
        }else if(pointsShape.j>0 && board[pointsShape.i][pointsShape.j-1]!=4){
            pointsShape.j--;
        }
        else if(pointsShape.i > 0  && board[pointsShape.i-1][pointsShape.j]!=4){
            pointsShape.i--;
        }
        else if(pointsShape.j < 9  && board[pointsShape.i][pointsShape.j+1]!=4){
            pointsShape.j++;
        }

    }else if(randNum < 0.75){
         if(pointsShape.i > 0  && board[pointsShape.i-1][pointsShape.j]!=4){
            pointsShape.i--;
        }
        else if(pointsShape.j < 9  && board[pointsShape.i][pointsShape.j+1]!=4){
            pointsShape.j++;
        }
        else if(pointsShape.i < 9  && board[pointsShape.i+1][pointsShape.j]!=4){
            pointsShape.i++;
        }else if(pointsShape.j>0 && board[pointsShape.i][pointsShape.j-1]!=4){
            pointsShape.j--;
        }
    }else{
         if(pointsShape.j < 9  && board[pointsShape.i][pointsShape.j+1]!=4){
            pointsShape.j++;
        }
        else if(pointsShape.i > 0  && board[pointsShape.i-1][pointsShape.j]!=4){
            pointsShape.i--;
        }
        
        else if(pointsShape.i < 9  && board[pointsShape.i+1][pointsShape.j]!=4){
            pointsShape.i++;
        }else if(pointsShape.j>0 && board[pointsShape.i][pointsShape.j-1]!=4){
            pointsShape.j--;
        }

    }


}


