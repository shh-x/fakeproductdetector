(function () {
  'use strict';

  const VIEWS = {
    GENUINE:     { css: 'ok',   title: '\u2714 Genuine product' },
    SUSPICIOUS:  { css: 'warn', title: '\u26A0 Be careful' },
    BLOCKED:     { css: 'bad',  title: '\u2716 Code disabled' },
    COUNTERFEIT: { css: 'bad',  title: '\u2716 Counterfeit or unknown code' },
    ERROR:       { css: 'warn', title: 'Could not check right now' }
  };

  const scannerBox = document.getElementById('scanner');
  const resultBox  = document.getElementById('result');
  const verdictEl  = document.getElementById('verdict');
  const detailEl   = document.getElementById('detail');
  const cameraMsg  = document.getElementById('camera-msg');
  const fileInput  = document.getElementById('file-input');
  const manualInput = document.getElementById('manual-token');

  const scanner = new Html5Qrcode('reader');
  let running = false;   // is the camera on?
  let busy = false;      // are we already handling a scan?

  function describe(d) {
    const what = d.productName ? d.productName + ' by ' + d.companyName + '. ' : '';
    switch (d.result) {
      case 'GENUINE':
        return what + 'This code has been scanned ' + d.scanCount + ' time(s) so far.';
      case 'SUSPICIOUS':
        return what + 'This code has been scanned ' + d.scanCount +
               ' times, which is unusually many. It may have been copied. ' +
               'Check the packaging carefully or contact the manufacturer.';
      case 'BLOCKED':
        return what + 'The manufacturer has disabled this code. Please contact them before using this product.';
      case 'COUNTERFEIT':
        return 'This code is not in the manufacturer\u2019s database. The product may be fake.';
      default:
        return 'Please try again in a moment.';
    }
  }

  function show(d) {
    const v = VIEWS[d.result] || VIEWS.ERROR;
    resultBox.className = v.css;
    verdictEl.textContent = v.title;          // textContent, never innerHTML (see break-it #5)
    detailEl.textContent = describe(d);
    scannerBox.hidden = true;
    resultBox.hidden = false;
  }

  function check(text) {
    const token = (text || '').trim().slice(0, 200);
    return fetch('verify', { method: 'POST', body: new URLSearchParams({ token: token }) })
      .then(r => r.json())
      .then(show)
      .catch(() => show({ result: 'ERROR' }));
  }

  function startCamera() {
    cameraMsg.textContent = '';
    return scanner.start(
      { facingMode: 'environment' },
      { fps: 10, qrbox: { width: 240, height: 240 } },
      onDecoded,
      () => { /* runs on every frame with no QR in it: ignore */ }
    ).then(() => { running = true; })
     .catch(() => {
       running = false;
       cameraMsg.textContent = 'Could not open the camera. Allow camera access, ' +
         'or use the photo upload / type-the-code options below.';
     });
  }

  function stopCamera() {
    if (!running) return Promise.resolve();
    running = false;
    return scanner.stop().catch(() => {});
  }

  function onDecoded(text) {
    if (busy) return;          // the camera reports the SAME QR many times per second
    busy = true;
    stopCamera().then(() => check(text)).finally(() => { busy = false; });
  }

  fileInput.addEventListener('change', () => {
    const file = fileInput.files[0];
    if (!file || busy) return;
    busy = true;
    stopCamera()
      .then(() => scanner.scanFile(file, false))
      .then(text => check(text))
      .catch(() => {
        cameraMsg.textContent = 'No QR code found in that photo. Try a clearer one.';
        return startCamera();
      })
      .finally(() => { busy = false; fileInput.value = ''; });
  });

  document.getElementById('manual-btn').addEventListener('click', () => {
    const text = manualInput.value.trim();
    if (!text || busy) return;
    busy = true;
    stopCamera().then(() => check(text)).finally(() => { busy = false; });
  });

  document.getElementById('again').addEventListener('click', () => {
    resultBox.hidden = true;
    scannerBox.hidden = false;
    startCamera();
  });

  startCamera();
})();