#!/usr/bin/env bash

remote_name="origin"
if git ls-remote --exit-code --heads "$remote_name" "master"; then
  master_branch="master"
fi
if git ls-remote --exit-code --heads "$remote_name" "main"; then
  master_branch="main"
fi
if [ -n "${master_branch}" ]; then
  git checkout "${master_branch}"
fi
git pull
git fetch -p
for branch_name in $(git branch -r | grep -v '\->' | sed 's/origin\///' | grep -v 'master' | grep -v 'main' | grep -v 'migration-to-kotlin' | grep -v '1.0.0-eol-continuous-release-branch-recovered' | grep -v 'migrate-to-kotlin'); do
  echo "Processing branch: $branch_name"
  if [ -n "${master_branch}" ]; then
    git checkout "${branch_name}"
    git pull
    git merge origin/"${master_branch}" --no-edit
    git push
    gh pr merge $(gh pr list --base "${master_branch}" --head "${branch_name}" --json number --jq '.[0].number' | xargs echo) --auto --merge
    git checkout "${master_branch}"
  fi
done
