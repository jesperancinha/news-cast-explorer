#!/bin/bash
git fetch -p
for branch in $(git branch | grep -v master)
do
    git checkout $branch
#    git pull
#    git merge --strategy-option theirs
#    git checkout master
#    git merge $branch
done

git checkout master
