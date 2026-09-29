require('dotenv').config();
const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const multer = require('multer');
const path = require('path');
const fs = require('fs');

const app = express();
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({extended:true}));

// cria pasta uploads se não existir
if (!fs.existsSync(path.join(__dirname, 'uploads'))) {
  fs.mkdirSync(path.join(__dirname, 'uploads'));
}
if (!fs.existsSync(path.join(__dirname, 'frontend', 'uploads'))) {
  try { fs.mkdirSync(path.join(__dirname, 'frontend', 'uploads')); } catch(e) {}
}

const storage = multer.diskStorage({
  destination: (req,file,cb)=>cb(null,'uploads/'),
  filename: (req,file,cb)=>cb(null, Date.now() + path.extname(file.originalname))
});
const upload = multer({storage});

const db = mysql.createConnection({
  host: process.env.DB_HOST  ,
  user: process.env.DB_USER  ,
  password: process.env.DB_PASSWORD  ,
  database: process.env.DB_NAME  ,
  port: process.env.DB_PORT 
});

db.connect(err=>{
  if(err) throw err;
  console.log('Conectado no artesanatos de rua - TESTE NOVO');
  console.log('USER:',process.env.DB_USER);
  console.log('HOST:',process.env.DB_HOST);
});

// SERVIR O FRONTEND - ESSA ERA A PARTE QUE FALTAVA
app.use(express.static(path.join(__dirname, 'frontend')));
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));
app.use('/uploads', express.static(path.join(__dirname, 'frontend', 'uploads')));

// SUAS ROTAS DA API (mantenha as que você já tem)
// Exemplo:
app.get('/api/produtos', (req, res) => {
  db.query('SELECT * FROM produtos', (err, result) => {
    if(err) return res.status(500).json(err);
    res.json(result);
  });
});

// IMPORTANTE: essa rota tem que ser a ÚLTIMA de todas
  app.use((req, res) => {
    res.sendFile(path.join(__dirname, 'frontend', 'index.html'));
  });
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Servidor rodando na porta ${PORT}`);
});