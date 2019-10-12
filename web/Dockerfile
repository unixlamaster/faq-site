FROM node:7
EXPOSE 8000
WORKDIR /faq-site
COPY src /faq-site
RUN npm install
RUN npm install express mongodb body-parser --save
RUN npm install --save-dev nodemon
CMD npm run dev > app.log
