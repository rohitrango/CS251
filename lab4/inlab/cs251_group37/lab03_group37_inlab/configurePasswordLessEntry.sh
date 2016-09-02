#! /bin/bash

ssh-keygen -t rsa
ssh $1@$2 -t 'exit'
ssh-copy-id -i ~/.ssh/id_rsa.pub $1@$2
ssh-add


