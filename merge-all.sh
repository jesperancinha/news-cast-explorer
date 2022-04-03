#!/bin/bash
git fetch -p
all_branches=$(git branch | grep -v master)

for branch in all_branches
do
    git checkout $branch
    git pull
    git merge --strategy-option theirs
    git checkout master
    git merge $branch
done

git checkout master
