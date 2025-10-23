FROM node:alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
RUN npm install react-router-dom
COPY . .
EXPOSE 5173
ENTRYPOINT ["npm", "run", "dev"]