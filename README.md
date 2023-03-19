![banner_poo-01](https://user-images.githubusercontent.com/72265977/226167767-788dce98-aff7-4bd2-bf3b-38a57e939fd5.png)

# Introducere
Jocul preia cele mai bune lucruri din Hearthstone și din Gwent, fiind mult mai ușor de implementat, dar și de jucat. De asemenea, ca în orice joc de calitate, o să ne asigurăm că avem și o poveste pe cinste pentru personajele noastre create.


# Introducere
Dorim ca jocul nostru să poată fi jucat de doi jucători în sistem amical. Cu toate acestea, pentru că încă suntem în etapa de dezvoltare a jocului în cadrul studioului nostru OOP-Everything, vom folosi un AI care va simula cei doi jucători. AI-ul vă va da comenzile de joc în input, împreună cu server-ul care vă va trimite și comenzi de debugging. Comenzile de debugging sunt făcute pentru a verifica statutul jocului în diverse etape, astfel vom putea depana mai ușor problemele care pot apărea în cadrul implementării. De asemenea, server-ul vă solicită și date pe care le va folosi pentru a crea statistici, cum ar fi numărul de câștiguri ale unui jucător și numărul total de jocuri terminate de cei doi jucători. La output dorim să printăm orice informație ne cere server-ul sau orice eroare întâmpinăm în cadrul unei acțiuni invalide.

# Masa de joc
Masa de joc este locul unde cărțile vor interacționa direct între ele. Ea poate fi reprezentată ca o matrice de 4×5, fiecare jucător având 2 rânduri asignate lui. Cărțile se plasează pe câte un rând conform restricțiilor impuse mai jos, fiind introduse pe rând de la stânga la dreapta, mai exact adăugăm mereu o carte la dreapta ultimei cărți adăugate până se umple rândul. Dacă o carte este omorâtă, atunci toate cărțile din dreapta ei vor fi mutate cu o poziție la stânga (fenomen cunoscut ca shiftare la stânga). De asemenea, pe un rând pot exista maxim 5 cărți.

Rândurile 0 și 1 sunt asignate jucătorului 2, iar rândurile 2 și 3 sunt asignate jucătorului 1, conform imaginii de mai jos. Rândurile din față vor fi reprezentate de rândurile 1 și 2, iar rândurile din spate vor fi 0 si 3 (jucătorii vor fi așezați față în față). Totodată, eroii jucătorilor vor avea un loc special în care vor fi așezați de la începutul jocului.

# Formatul cărților de joc
Există mai multe tipuri de cărți pe care un jucător le poate deține, mai exact un jucător poate avea o carte de tipul minion sau o carte de tipul **environment**. De asemenea, fiecare jucător va avea o carte specială de tipul erou care îl va reprezenta în joc.


  ### Cartea Minion
  Aceste cărți au la bază atribute precum: mana (intreg), health (intreg), attackDamage (intreg), description (String), colors (String/Enum) si name (String). Mana   este costul pe care îl va avea o carte pentru a putea fi folosită în timpul jocului, health reprezintă viața unei cărți, attackDamage reprezintă punctele de atac cu care o carte poate ataca altă carte, description este o descriere scurta a cărții, colors reprezintă culorile care alcătuiesc design-ul grafic al cărții și name este numele cărții. Aceste atribute le veți primi la input, fiecare carte având un set de valori specifice care vor fi randomizate. Drept urmare, două cărți cu același nume pot avea proprietăți diferite.

  Din acest tipar fac parte cărțile următoare: Sentinel, Berserker, Goliath și Warden.

  Pe lângă aceste cărți, mai există și cărți care posedă abilități speciale. Aceste cărți sunt: Miraj, The Ripper, Disciple și The Cursed One. Abilitățile speciale ale   acestor cărți vor fi targetate către alte cărți pentru a activa următoarele efecte:

 **The Ripper**
 
Weak Knees: -2 atac pentru un minion din tabăra adversă.

 **Miraj**
 
Skyjack: face swap între viața lui și viața unui minion din tabăra adversă.

 **The Cursed One**
 
Shapeshift: face swap între viața unui minion din tabăra adversă și atacul aceluiași minion

 **Disciple**
 
God's Plan: +2 viață pentru un minion din tabăra lui.


### Cartea Environment

  Aceste cărți au la bază atribute precum: mana, description, colors și name. Ele reprezintă efecte care pot fi aplicate terenului de joc, în povestea noastră fiind efecte naturale ale unei lumi din povești. Fiecare efect ia în considerare toate cărțile de pe rândul targetat. Cărțile de tip environment sunt următoarele:

  **Firestorm**
  
Scade cu 1 viața tuturor minionilor de pe rând.

  **Winterfell**
  
Toate cărțile de pe rând stau o tură.

  **Heart Hound**

Se fură minionul adversarului cu cea mai mare viață de pe rând și se pune pe rândul “oglindit” aferent jucătorului.


  ### Cartea Erou
  
  Există 4 eroi pe care un jucator îi poate avea. Fiecare erou are următoarele atribute: mana, description, colors și name. Fiecare erou are parametrul health fixat la 30. Dacă eroul ajunge în timpul jocului să fie atacat și să aibe viața 0, jocul se va opri și va câștiga atacatorul. Eroul fiecărui jucător este specificat în input la începutul jocului. Totodată, ei au abilități speciale care sunt declanșate pe un rând:

  **Lord Royce**
  
Sub-Zero: îngheață cartea cu cel mai mare atac de pe rând.

  **Empress Thorina**

Low Blow: distruge cartea cu cea mai mare viață de pe rând.

  **King Mudface**

Earth Born: +1 viață pentru toate cărțile de pe rând.

  **General Kocioraw**

Blood Thirst: +1 atac pentru toate cărțile de pe rând.


# Pregătirea jocului

Fiecare jucător își alege un deck, acesta fiind specificat de AI în input. Deck-ul ales de fiecare jucător va fi amestecat la începutul jocului, deoarece dorim ca jocul sa nu fie previzibil. Pentru a ne asigura că rezultatele vor fi deterministe, server-ul va trimite la input și un seed folosit ca parametru pentru procesul de amestecare.

Server-ul alege câte un erou aleator pentru fiecare jucător, aceștia fiind specificați la input.

Se alege un jucător care să înceapă prima rundă, fiind specificat la input.

# Începutul unei runde
La începutul fiecărei runde, ambii jucători primesc prima carte disponibilă din pachetul de cărți ales. Cartea primită de fiecare jucător va fi adăugată în “mâna” acestuia. Cartea primită este adăugată la sfârșitul listei de cărți din mână.

Dacă nu mai există cărți în pachet, nu mai trebuie trasă nicio carte în mână.

# Sfârșitul unei ture

La finalul turei unui jucător, cărțile acestuia care au fost marcate ca fiind “frozen” sunt demarcate, întrucât acestea au stat o tura.

Totodată, se verifică dacă ambii jucători și-au sfârșit turele în cadrul rundei curente. Dacă această condiție este adevarată, se trece la următoarea rundă si se aplică pașii descriși mai sus.

Marcarea sfârșitului unei ture va fi precizată explicit de AI în input pentru jucătorul curent.
