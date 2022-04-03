#!/bin/bash
git fetch -p
git branch | grep -v master |  xargs -I {} git checkout {} &&
  git pull &&
  git merge --strategy-option theirs &&
  git checkout master &&
  git merge {}
