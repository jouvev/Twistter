import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import MainPage from './App';
import * as serviceWorker from './serviceWorker';

window.addressServer = 'http://vps578770.ovh.net:8080/Twistter';
//window.addressServer = 'http://localhost:8080/Twistter';

ReactDOM.render(<MainPage />, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
