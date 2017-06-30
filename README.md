# risk_awareness_simulation_javafx
## Introduction
In the field of robotic control, one of the most challenging problems faced by all controllers is called receding horizon. 
This phenomenon is when the robot loses contact with its controllers for a brief moment of time due to environmental obstacles 
whereas the risk of the robot running into danger accumulates. The goal of this project is to find out under this special condition, 
whether it is preferrable to rely on a set of instructions previously set by human, or to completely rely on the robot's internal 
algorithm to decide next step of action.  

## User Interface
<img src="https://github.com/YuansongFeng/risk_awareness_simulation_javafx/blob/master/screenshots/control.png" width="400">
<img src="https://github.com/YuansongFeng/risk_awareness_simulation_javafx/blob/master/screenshots/eval.png" width="400">
<img src="https://github.com/YuansongFeng/risk_awareness_simulation_javafx/blob/master/screenshots/feedback.png" width="400">
      
## Features
+ Consecutive path planning based on Dijkstra in the maze of dangerous blocks
+ Incoportate path deviation approximated by a Normal Distribution in executing the path, which mimics the real life error
+ Use hidden Markov analysis to analyze the participants' choosing behavior in the background

