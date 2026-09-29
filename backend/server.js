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

if (!fs.existsSync('uploads')) fs.mkdirSync('uploads');


const storage = multer.diskStorage({
  destination: (req,file,cb)=>cb(null,'uploads/'),
  filename: (req,file,cb)=>cb(null, Date.now() + path.extname(file.originalname))
});
const upload = multer({storage});

const db = mysql.createConnection({
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || '333405',
  database: process.env.DB_NAME || 'artesanatos_de_rua',
  port: process.env.DB_PORT || 3306
});

db.connect(err=>{
  if(err) throw err;
  console.log('Conectado no artesanatos_de_rua');
});

app.use(express.static(path.join(__dirname, '../frontend')));
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

app.post('/api/upload', upload.single('foto'), (req,res)=>{
  res.json({url:'/uploads/'+req.file.filename});
});

app.post('/api/produtos', (req,res)=>{
  const {nome,categoria,preco,custo,quantidade,imagem,imagem2,imagem3,descricao} = req.body;
  db.query('INSERT INTO artesanato (nome,categoria,preco,custo,quantidade,imagem,imagem2,imagem3,descricao) VALUES (?,?,?,?,?,?,?,?,?)',
  [nome,categoria,preco,custo,quantidade,imagem,imagem2,imagem3,descricao],
  (err)=>{
    if(err) return res.status(500).json(err);
    res.json({ok:true});
  });
});

app.get('/api/produtos', (req,res)=>{
  db.query('SELECT *, (preco - custo) as lucro, (preco - custo)*quantidade as lucro_total FROM artesanato ORDER BY id DESC', (err, results)=>{
    if(err) return res.status(500).json(err);
    res.json(results);
  });
});

app.delete('/api/produtos/:id', (req,res)=>{
  db.query('DELETE FROM artesanato WHERE id=?', [req.params.id], (err)=>{
    if(err) return res.status(500).json(err);
    res.json({ok:true});
  });
});

const path = require('path');
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));
app.use(express.static(path.join(__dirname, 'frontend')));
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'frontend/index.html'));
});
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => console.log('Loja no ar na porta ' + PORT));