FROM maven

COPY .git /home/.git
WORKDIR /home
RUN git reset --hard
RUN ./scripts/run_tests.sh


