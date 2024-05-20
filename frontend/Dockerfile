FROM node:20-buster
WORKDIR /src/app

COPY package.json .
COPY yarn.lock .

RUN yarn

COPY . .

RUN yarn build

EXPOSE 3000

CMD ["yarn", "start"]
