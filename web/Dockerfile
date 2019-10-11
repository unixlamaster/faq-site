FROM node:7
EXPOSE 8000
RUN git clone https://github.com/unixlamaster/faq-site.git
WORKDIR /faq-site
COPY . /faq-site
RUN npm install
RUN npm install express mongodb body-parser --save
RUN npm install --save-dev nodemon
CMD npm run dev > app.log
