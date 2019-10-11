FROM node:7
RUN git clone https://github.com/unixlamaster/faq-site.git
WORKDIR /faq-site
COPY package.json /faq-site
RUN npm install
RUN npm install express mongodb body-parser --save
RUN npm install --save-dev nodemon
EXPOSE 8000
CMD npm run dev > app.log
