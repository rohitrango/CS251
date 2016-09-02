#!/bin/bash
# Please use 'source' command to check the correctness of the script
# For task A, sub-task 3, we change the PS1 variable so that it shows the required format.

if [ "$color_prompt" = yes ]; then
    PS1='${debian_chroot:+($debian_chroot)}\[\033[01;32m\]\u@\h\[\033[00m\] - \[\033[01;34m\]\t [\W]\[\033[00m\]:$ '
else
	
	PS1='${debian_chroot:+($debian_chroot)}\u@\h - \t [\W]:$ '
fi

# For task A, sub-task 4, we gave an alias for bk command using $OLDPWD variable.
# Given an alias
alias bk='cd "$OLDPWD"'

# For task A, sub-task 5, we made a custom Welcome message which is color-coded for beautification.
# Echo with color codes - \e[31m - red, \e[92m - green , \e[0m - white
echo -e '\e[31m   / / / /__  / / /___        
  / /_/ / _ \/ / / __ \       
 / __  /  __/ / / /_/ /       
/_/ /_/\___/_/_/\____/
\e[92m
| |     / /___  _____/ /___/ /
| | /| / / __ \/ ___/ / __  / 
| |/ |/ / /_/ / /  / / /_/ /  
|__/|__/\____/_/  /_/\__,_/   
\e[0m'