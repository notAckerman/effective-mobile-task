FROM ubuntu:latest
LABEL authors="Ken Kaneki"

ENTRYPOINT ["top", "-b"]