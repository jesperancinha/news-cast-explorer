#!/bin/bash
set -e
git fetch -p
for branch in $(git branch | grep -v master)
do
    git checkout "$branch"
    git pull
    git merge --strategy-option theirs
    git checkout master
    git merge "$branch"
done

for branch in $(git branch | grep -v master)
do
    git branch -d "$branch"
    git push origin :refs/branches/"$branch"
done

git checkout master
