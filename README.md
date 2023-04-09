# Orders-Processing-using-Replicated-Workers
#Copyright Stan Andreea-Cristina 333CA

Tema2 
    main -> Metoda in care se afla toata logica main, se face citirea din
            linia de comanda si se deschid thread-urile pentru executie.
            Pentru a nu se appendui informatie de la un fisier la altu in
            cadru fiecarui test, am declarat doua fisiere ca sa fie noi la
            fiecare nou test.
            Am pornit thread-urile le-am dat start() ca sa inceapa lucrul,
            urmand ca dupa ce termina sa le dau join().

Orders
    run -> Aici se afla logica pentru thread-urile de level 1, care realizeaza
            citirea comenzilor din fisier caracter cu caracter, iar apoi imparte
            string-ul in numele comenzii si numarul de produse, ca mai apoi sa
            se apeleze functia pentru thread-urile de level 2 pentru a incepe
            cautarea de produse in fisier.
            Cand se afla numarul de produse total, se parcurg acestea si se apeleaza
            functia potrivita, iar la final, cand toate thread-urile au terminat
            de prelucrat produsele, se scrie in fisierul de comenzi, avand grija sa se
            realizeze sincronizarea asa cum trebuie.

Products
    run -> Aici se afla logica pentru thread-urile de level 2, unde se realizeaza citirea
            din fisierul cu produse pana in momentul in care se gaseste produsul cu id-ul
            potrivit, apoi se realizeaza scrierea in fisierul potrivit, respectandu-se la
            fel sincronizarea, asa incat sa nu scrie 2 thread-uri in acelasi timp in acelasi
            fisier.
            
            
Pentru implementarea bonusului am inceput prin a calcula dimensiunea totala a fisierului ce va fi prelucrat de comenzi, apoi impartit
la numarul de thread-uri pentru a stabili de la inceput cat calup deinformatie va avea de parcurs fiecare.
In cadrul primei metode run unde se afla logica threadurilor de level
1 dau skip la caracterele din fisier pana ajung la caracterul de pe
pozitia start.
Apoi, impart problema in 3 cazuri :
-ajung la primul '\n' si verific daca comanda contine "o_", daca
contine asta, inseamna ca este o comanda completa si o pot prelucra.
-ajung la '\n' si nu contine "o_", ceea ce inseamna ca nu e
o comanda completa si sar peste ea.
-ajung la '\n' si ma aflu pe pozitia end - 1, ceea ce inseamna ca
am inceput deja sa iau o comanda si ma aflu in mijlocul ei, deci 
nu-i dau skip, ci o prelucrez si pe ea in cadrul thread-ului curent.
