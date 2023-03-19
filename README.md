![banner_poo-01](https://user-images.githubusercontent.com/72265977/226167767-788dce98-aff7-4bd2-bf3b-38a57e939fd5.png)

# Introducere
Jocul preia cele mai bune lucruri din Hearthstone și din Gwent, fiind mult mai ușor de implementat, dar și de jucat. De asemenea, ca în orice joc de calitate, o să ne asigurăm că avem și o poveste pe cinste pentru personajele noastre create.


# Introducere
Dorim ca jocul nostru să poată fi jucat de doi jucători în sistem amical. Cu toate acestea, pentru că încă suntem în etapa de dezvoltare a jocului în cadrul studioului nostru OOP-Everything, vom folosi un AI care va simula cei doi jucători. AI-ul vă va da comenzile de joc în input, împreună cu server-ul care vă va trimite și comenzi de debugging. Comenzile de debugging sunt făcute pentru a verifica statutul jocului în diverse etape, astfel vom putea depana mai ușor problemele care pot apărea în cadrul implementării. De asemenea, server-ul vă solicită și date pe care le va folosi pentru a crea statistici, cum ar fi numărul de câștiguri ale unui jucător și numărul total de jocuri terminate de cei doi jucători. La output dorim să printăm orice informație ne cere server-ul sau orice eroare întâmpinăm în cadrul unei acțiuni invalide.

# Masa de joc
Masa de joc este locul unde cărțile vor interacționa direct între ele. Ea poate fi reprezentată ca o matrice de 4×5, fiecare jucător având 2 rânduri asignate lui. Cărțile se plasează pe câte un rând conform restricțiilor impuse mai jos, fiind introduse pe rând de la stânga la dreapta, mai exact adăugăm mereu o carte la dreapta ultimei cărți adăugate până se umple rândul. Dacă o carte este omorâtă, atunci toate cărțile din dreapta ei vor fi mutate cu o poziție la stânga (fenomen cunoscut ca shiftare la stânga). De asemenea, pe un rând pot exista maxim 5 cărți.

Rândurile 0 și 1 sunt asignate jucătorului 2, iar rândurile 2 și 3 sunt asignate jucătorului 1, conform imaginii de mai jos. Rândurile din față vor fi reprezentate de rândurile 1 și 2, iar rândurile din spate vor fi 0 si 3 (jucătorii vor fi așezați față în față). Totodată, eroii jucătorilor vor avea un loc special în care vor fi așezați de la începutul jocului.

# Formatul cărților de joc
Există mai multe tipuri de cărți pe care un jucător le poate deține, mai exact un jucător poate avea o carte de tipul minion sau o carte de tipul *environment*. De asemenea, fiecare jucător va avea o carte specială de tipul erou care îl va reprezenta în joc.

