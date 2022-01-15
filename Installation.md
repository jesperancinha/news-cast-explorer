# News Cast Explorer Installation notes

## Renovate

##### Env Vars

`RENOVATE_CONFIG`, `RENOVATE_PASSWORD`

##### Config

```json
{
  "platform": "bitbucket",
  "username": "jesperancinha"
}
```

##### Commands

```shell
export RENOVATE_CONFIG='{
  "platform": "bitbucket",
  "username": "jesperancinha"
}'
export RENOVATE_PASSWORD=<TOKEN>
renovate --autodiscover
```
