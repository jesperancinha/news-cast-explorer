# News Cast Explorer LogBook

<b>2022/08/10<b>
After much deliberation and consideration, I have decided to remove this project from bitbucket.

Here is a list of the reasoning behind it:

1. Bitbucket pipelines are very limited. It is impossible to use Renovate consistently enough without expiring the reduced amount of free build time credits per month.
2. As an alternative I attached CircleCI to it. However, Circle CI also have a vey important, subtle, but crucial limitation. Although we can make maven builds with the JDK17, I couldn not find a suitable machine provided by CircleCI to perform my Test Container tests. They need a Docker environment for that. None of them offered JDK17 by default. Instead, I had to manually install the JDK17 in the latest image for every single build.

With these limitations I decided to wrap-up the usages of bitbucket for this project and now I'm moving it to gitlab along with [other projects](https://gitlab.com/jesperancinha) I have.

Check the [config.yml](./backup/.circleci/config.yml) to have an idea about this.