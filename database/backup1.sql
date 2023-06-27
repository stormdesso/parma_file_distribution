PGDMP                         {            testERD    15.3    15.3 Y    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16422    testERD    DATABASE     }   CREATE DATABASE "testERD" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';
    DROP DATABASE "testERD";
                postgres    false            Z           1247    16472    roles    TYPE     R   CREATE TYPE public.roles AS ENUM (
    'USER',
    'ADMIN_SCOPES',
    'ADMIN'
);
    DROP TYPE public.roles;
       public          postgres    false            �            1259    17451    file    TABLE     �   CREATE TABLE public.file (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    size double precision NOT NULL,
    type character varying NOT NULL,
    "dateCreated" date NOT NULL,
    location character varying NOT NULL
);
    DROP TABLE public.file;
       public         heap    postgres    false            �            1259    17418    file_for_version    TABLE     �   CREATE TABLE public.file_for_version (
    id integer NOT NULL,
    version_id integer NOT NULL,
    file_id integer NOT NULL
);
 $   DROP TABLE public.file_for_version;
       public         heap    postgres    false            �            1259    17450    file_id_seq    SEQUENCE     �   ALTER TABLE public.file ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    231            �            1259    17386    folder    TABLE     �   CREATE TABLE public.folder (
    id integer NOT NULL,
    scope_id integer NOT NULL,
    title character varying(50) NOT NULL,
    publish boolean NOT NULL,
    "manifest_IOS" boolean NOT NULL,
    identifier character varying(30) NOT NULL
);
    DROP TABLE public.folder;
       public         heap    postgres    false            �            1259    17385    folder_id_seq    SEQUENCE     �   ALTER TABLE public.folder ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.folder_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    221            �            1259    17378    illustration_for_scope    TABLE     �   CREATE TABLE public.illustration_for_scope (
    id integer NOT NULL,
    scope_id integer NOT NULL,
    file_id integer NOT NULL
);
 *   DROP TABLE public.illustration_for_scope;
       public         heap    postgres    false            �            1259    17377    illustration_for_scope_id_seq    SEQUENCE     �   ALTER TABLE public.illustration_for_scope ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.illustration_for_scope_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    219            �            1259    17459    illustration_for_version    TABLE     �   CREATE TABLE public.illustration_for_version (
    id integer NOT NULL,
    version_id integer NOT NULL,
    file_id integer NOT NULL
);
 ,   DROP TABLE public.illustration_for_version;
       public         heap    postgres    false            �            1259    17458    illustration_for_version_id_seq    SEQUENCE     �   ALTER TABLE public.illustration_for_version ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.illustration_for_version_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    233            �            1259    17366     license_agreement_file_for_scope    TABLE       CREATE TABLE public.license_agreement_file_for_scope (
    id integer NOT NULL,
    name character varying(250) NOT NULL,
    comment character varying(1000),
    size double precision NOT NULL,
    content bytea NOT NULL,
    scope_id integer NOT NULL,
    file_id integer NOT NULL
);
 4   DROP TABLE public.license_agreement_file_for_scope;
       public         heap    postgres    false            �            1259    17365 '   license_agreement_file_for_scope_id_seq    SEQUENCE     �   ALTER TABLE public.license_agreement_file_for_scope ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.license_agreement_file_for_scope_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    217            �            1259    17393    manifest_for_IOS    TABLE     �   CREATE TABLE public."manifest_for_IOS" (
    id integer NOT NULL,
    folder_id integer NOT NULL,
    name character varying(250) NOT NULL,
    content bytea NOT NULL
);
 &   DROP TABLE public."manifest_for_IOS";
       public         heap    postgres    false            �            1259    17467    role    TABLE     y   CREATE TABLE public.role (
    id integer NOT NULL,
    user_id integer NOT NULL,
    role_name public.roles NOT NULL
);
    DROP TABLE public.role;
       public         heap    postgres    false    858            �            1259    17466    role_id_seq    SEQUENCE     �   ALTER TABLE public.role ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    235            �            1259    17356    scope    TABLE     �   CREATE TABLE public.scope (
    id integer NOT NULL,
    title character varying(20) NOT NULL,
    description character varying(1000),
    copyright character varying(40),
    show_illustrations boolean NOT NULL
);
    DROP TABLE public.scope;
       public         heap    postgres    false            �            1259    17355    scope_id_seq    SEQUENCE     �   ALTER TABLE public.scope ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.scope_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    215            �            1259    17411    tag    TABLE     �   CREATE TABLE public.tag (
    id integer NOT NULL,
    letter character varying(250) NOT NULL,
    bkg_color character varying NOT NULL,
    letter_color character varying NOT NULL
);
    DROP TABLE public.tag;
       public         heap    postgres    false            �            1259    17425    tag_file_for_version    TABLE     t   CREATE TABLE public.tag_file_for_version (
    tag_id integer NOT NULL,
    file_for_version_id integer NOT NULL
);
 (   DROP TABLE public.tag_file_for_version;
       public         heap    postgres    false            �            1259    17445 
   user_scope    TABLE     `   CREATE TABLE public.user_scope (
    user_id integer NOT NULL,
    scope_id integer NOT NULL
);
    DROP TABLE public.user_scope;
       public         heap    postgres    false            �            1259    17444    user_scope_scope_id_seq    SEQUENCE     �   ALTER TABLE public.user_scope ALTER COLUMN scope_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_scope_scope_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    229            �            1259    17430    username    TABLE       CREATE TABLE public.username (
    id integer NOT NULL,
    name character varying(40) NOT NULL,
    password character varying(50) NOT NULL,
    blocked boolean DEFAULT false NOT NULL,
    is_admin_manager boolean DEFAULT false NOT NULL,
    is_admin_scope_manager boolean DEFAULT false NOT NULL,
    can_create_and_delete_scope boolean DEFAULT false,
    max_number_scope integer DEFAULT 0,
    max_storage_space integer DEFAULT 0,
    max_number_folder integer DEFAULT 0,
    role character varying NOT NULL
);
    DROP TABLE public.username;
       public         heap    postgres    false            �            1259    17402    version    TABLE     S  CREATE TABLE public.version (
    id integer NOT NULL,
    version_number character varying(10) NOT NULL,
    date_of_publication date NOT NULL,
    description character varying(1000),
    show_illustration boolean NOT NULL,
    "URLLicense" character varying(25) NOT NULL,
    publish boolean NOT NULL,
    folder_id integer NOT NULL
);
    DROP TABLE public.version;
       public         heap    postgres    false            �          0    17451    file 
   TABLE DATA           M   COPY public.file (id, name, size, type, "dateCreated", location) FROM stdin;
    public          postgres    false    231   Pw       �          0    17418    file_for_version 
   TABLE DATA           C   COPY public.file_for_version (id, version_id, file_id) FROM stdin;
    public          postgres    false    225   mw       ~          0    17386    folder 
   TABLE DATA           Z   COPY public.folder (id, scope_id, title, publish, "manifest_IOS", identifier) FROM stdin;
    public          postgres    false    221   �w       |          0    17378    illustration_for_scope 
   TABLE DATA           G   COPY public.illustration_for_scope (id, scope_id, file_id) FROM stdin;
    public          postgres    false    219   �w       �          0    17459    illustration_for_version 
   TABLE DATA           K   COPY public.illustration_for_version (id, version_id, file_id) FROM stdin;
    public          postgres    false    233   �w       z          0    17366     license_agreement_file_for_scope 
   TABLE DATA           o   COPY public.license_agreement_file_for_scope (id, name, comment, size, content, scope_id, file_id) FROM stdin;
    public          postgres    false    217   �w                 0    17393    manifest_for_IOS 
   TABLE DATA           J   COPY public."manifest_for_IOS" (id, folder_id, name, content) FROM stdin;
    public          postgres    false    222   �w       �          0    17467    role 
   TABLE DATA           6   COPY public.role (id, user_id, role_name) FROM stdin;
    public          postgres    false    235   x       x          0    17356    scope 
   TABLE DATA           V   COPY public.scope (id, title, description, copyright, show_illustrations) FROM stdin;
    public          postgres    false    215   Ax       �          0    17411    tag 
   TABLE DATA           B   COPY public.tag (id, letter, bkg_color, letter_color) FROM stdin;
    public          postgres    false    224   ^x       �          0    17425    tag_file_for_version 
   TABLE DATA           K   COPY public.tag_file_for_version (tag_id, file_for_version_id) FROM stdin;
    public          postgres    false    226   {x       �          0    17445 
   user_scope 
   TABLE DATA           7   COPY public.user_scope (user_id, scope_id) FROM stdin;
    public          postgres    false    229   �x       �          0    17430    username 
   TABLE DATA           �   COPY public.username (id, name, password, blocked, is_admin_manager, is_admin_scope_manager, can_create_and_delete_scope, max_number_scope, max_storage_space, max_number_folder, role) FROM stdin;
    public          postgres    false    227   �x       �          0    17402    version 
   TABLE DATA           �   COPY public.version (id, version_number, date_of_publication, description, show_illustration, "URLLicense", publish, folder_id) FROM stdin;
    public          postgres    false    223   �x       �           0    0    file_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.file_id_seq', 1, false);
          public          postgres    false    230            �           0    0    folder_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.folder_id_seq', 1, false);
          public          postgres    false    220            �           0    0    illustration_for_scope_id_seq    SEQUENCE SET     L   SELECT pg_catalog.setval('public.illustration_for_scope_id_seq', 1, false);
          public          postgres    false    218            �           0    0    illustration_for_version_id_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.illustration_for_version_id_seq', 1, false);
          public          postgres    false    232            �           0    0 '   license_agreement_file_for_scope_id_seq    SEQUENCE SET     V   SELECT pg_catalog.setval('public.license_agreement_file_for_scope_id_seq', 1, false);
          public          postgres    false    216            �           0    0    role_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('public.role_id_seq', 1, true);
          public          postgres    false    234            �           0    0    scope_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.scope_id_seq', 1, false);
          public          postgres    false    214            �           0    0    user_scope_scope_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.user_scope_scope_id_seq', 1, false);
          public          postgres    false    228            �           2606    17424 6   file_for_version file_for_version_file_id_file_id1_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.file_for_version
    ADD CONSTRAINT file_for_version_file_id_file_id1_key UNIQUE (file_id) INCLUDE (file_id);
 `   ALTER TABLE ONLY public.file_for_version DROP CONSTRAINT file_for_version_file_id_file_id1_key;
       public            postgres    false    225            �           2606    17422 &   file_for_version file_for_version_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public.file_for_version
    ADD CONSTRAINT file_for_version_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY public.file_for_version DROP CONSTRAINT file_for_version_pkey;
       public            postgres    false    225            �           2606    17457    file file_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.file
    ADD CONSTRAINT file_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.file DROP CONSTRAINT file_pkey;
       public            postgres    false    231            �           2606    17401    manifest_for_IOS folder_id 
   CONSTRAINT     p   ALTER TABLE ONLY public."manifest_for_IOS"
    ADD CONSTRAINT folder_id UNIQUE (folder_id) INCLUDE (folder_id);
 F   ALTER TABLE ONLY public."manifest_for_IOS" DROP CONSTRAINT folder_id;
       public            postgres    false    222            �           2606    17390    folder folder_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.folder
    ADD CONSTRAINT folder_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.folder DROP CONSTRAINT folder_pkey;
       public            postgres    false    221            �           2606    17392    folder folder_title_title1_key 
   CONSTRAINT     j   ALTER TABLE ONLY public.folder
    ADD CONSTRAINT folder_title_title1_key UNIQUE (title) INCLUDE (title);
 H   ALTER TABLE ONLY public.folder DROP CONSTRAINT folder_title_title1_key;
       public            postgres    false    221            �           2606    17384 B   illustration_for_scope illustration_for_scope_file_id_file_id1_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.illustration_for_scope
    ADD CONSTRAINT illustration_for_scope_file_id_file_id1_key UNIQUE (file_id) INCLUDE (file_id);
 l   ALTER TABLE ONLY public.illustration_for_scope DROP CONSTRAINT illustration_for_scope_file_id_file_id1_key;
       public            postgres    false    219            �           2606    17382 2   illustration_for_scope illustration_for_scope_pkey 
   CONSTRAINT     p   ALTER TABLE ONLY public.illustration_for_scope
    ADD CONSTRAINT illustration_for_scope_pkey PRIMARY KEY (id);
 \   ALTER TABLE ONLY public.illustration_for_scope DROP CONSTRAINT illustration_for_scope_pkey;
       public            postgres    false    219            �           2606    17465 F   illustration_for_version illustration_for_version_file_id_file_id1_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.illustration_for_version
    ADD CONSTRAINT illustration_for_version_file_id_file_id1_key UNIQUE (file_id) INCLUDE (file_id);
 p   ALTER TABLE ONLY public.illustration_for_version DROP CONSTRAINT illustration_for_version_file_id_file_id1_key;
       public            postgres    false    233            �           2606    17463 6   illustration_for_version illustration_for_version_pkey 
   CONSTRAINT     t   ALTER TABLE ONLY public.illustration_for_version
    ADD CONSTRAINT illustration_for_version_pkey PRIMARY KEY (id);
 `   ALTER TABLE ONLY public.illustration_for_version DROP CONSTRAINT illustration_for_version_pkey;
       public            postgres    false    233            �           2606    17376 V   license_agreement_file_for_scope license_agreement_file_for_scope_file_id_file_id1_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.license_agreement_file_for_scope
    ADD CONSTRAINT license_agreement_file_for_scope_file_id_file_id1_key UNIQUE (file_id) INCLUDE (file_id);
 �   ALTER TABLE ONLY public.license_agreement_file_for_scope DROP CONSTRAINT license_agreement_file_for_scope_file_id_file_id1_key;
       public            postgres    false    217            �           2606    17372 F   license_agreement_file_for_scope license_agreement_file_for_scope_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.license_agreement_file_for_scope
    ADD CONSTRAINT license_agreement_file_for_scope_pkey PRIMARY KEY (id);
 p   ALTER TABLE ONLY public.license_agreement_file_for_scope DROP CONSTRAINT license_agreement_file_for_scope_pkey;
       public            postgres    false    217            �           2606    17399 &   manifest_for_IOS manifest_for_IOS_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public."manifest_for_IOS"
    ADD CONSTRAINT "manifest_for_IOS_pkey" PRIMARY KEY (id);
 T   ALTER TABLE ONLY public."manifest_for_IOS" DROP CONSTRAINT "manifest_for_IOS_pkey";
       public            postgres    false    222            �           2606    17471    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public            postgres    false    235            �           2606    17374 )   license_agreement_file_for_scope scope_id 
   CONSTRAINT     {   ALTER TABLE ONLY public.license_agreement_file_for_scope
    ADD CONSTRAINT scope_id UNIQUE (scope_id) INCLUDE (scope_id);
 S   ALTER TABLE ONLY public.license_agreement_file_for_scope DROP CONSTRAINT scope_id;
       public            postgres    false    217            �           2606    17362    scope scope_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.scope
    ADD CONSTRAINT scope_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.scope DROP CONSTRAINT scope_pkey;
       public            postgres    false    215            �           2606    17364    scope scope_title_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.scope
    ADD CONSTRAINT scope_title_key UNIQUE (title);
 ?   ALTER TABLE ONLY public.scope DROP CONSTRAINT scope_title_key;
       public            postgres    false    215            �           2606    17429 $   tag_file_for_version tag_and_version 
   CONSTRAINT     �   ALTER TABLE ONLY public.tag_file_for_version
    ADD CONSTRAINT tag_and_version UNIQUE (tag_id, file_for_version_id) INCLUDE (tag_id, file_for_version_id);
 N   ALTER TABLE ONLY public.tag_file_for_version DROP CONSTRAINT tag_and_version;
       public            postgres    false    226    226            �           2606    17417    tag tag_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.tag DROP CONSTRAINT tag_pkey;
       public            postgres    false    224            �           2606    17443    username user_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.username
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.username DROP CONSTRAINT user_pkey;
       public            postgres    false    227            �           2606    17449 =   user_scope user_scope_user_id_scope_id_user_id1_scope_id1_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.user_scope
    ADD CONSTRAINT user_scope_user_id_scope_id_user_id1_scope_id1_key UNIQUE (user_id, scope_id) INCLUDE (user_id, scope_id);
 g   ALTER TABLE ONLY public.user_scope DROP CONSTRAINT user_scope_user_id_scope_id_user_id1_scope_id1_key;
       public            postgres    false    229    229            �           2606    17410 G   version version_folder_id_version_number_folder_id1_version_number1_key 
   CONSTRAINT     �   ALTER TABLE ONLY public.version
    ADD CONSTRAINT version_folder_id_version_number_folder_id1_version_number1_key UNIQUE (folder_id, version_number) INCLUDE (folder_id, version_number);
 q   ALTER TABLE ONLY public.version DROP CONSTRAINT version_folder_id_version_number_folder_id1_version_number1_key;
       public            postgres    false    223    223            �           2606    17408    version version_pkey 
   CONSTRAINT     o   ALTER TABLE ONLY public.version
    ADD CONSTRAINT version_pkey PRIMARY KEY (id) INCLUDE (id, version_number);
 >   ALTER TABLE ONLY public.version DROP CONSTRAINT version_pkey;
       public            postgres    false    223    223            �           2606    17512 .   file_for_version file_for_version_file_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.file_for_version
    ADD CONSTRAINT file_for_version_file_id_fkey FOREIGN KEY (file_id) REFERENCES public.file(id) NOT VALID;
 X   ALTER TABLE ONLY public.file_for_version DROP CONSTRAINT file_for_version_file_id_fkey;
       public          postgres    false    3281    225    231            �           2606    17507 1   file_for_version file_for_version_version_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.file_for_version
    ADD CONSTRAINT file_for_version_version_id_fkey FOREIGN KEY (version_id) REFERENCES public.version(id) NOT VALID;
 [   ALTER TABLE ONLY public.file_for_version DROP CONSTRAINT file_for_version_version_id_fkey;
       public          postgres    false    223    225    3267            �           2606    17537    file file_id_fkey    FK CONSTRAINT     w   ALTER TABLE ONLY public.file
    ADD CONSTRAINT file_id_fkey FOREIGN KEY (id) REFERENCES public.version(id) NOT VALID;
 ;   ALTER TABLE ONLY public.file DROP CONSTRAINT file_id_fkey;
       public          postgres    false    231    223    3267            �           2606    17492    folder folder_scope_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.folder
    ADD CONSTRAINT folder_scope_id_fkey FOREIGN KEY (scope_id) REFERENCES public.scope(id) NOT VALID;
 E   ALTER TABLE ONLY public.folder DROP CONSTRAINT folder_scope_id_fkey;
       public          postgres    false    221    215    3243            �           2606    17487 :   illustration_for_scope illustration_for_scope_file_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.illustration_for_scope
    ADD CONSTRAINT illustration_for_scope_file_id_fkey FOREIGN KEY (file_id) REFERENCES public.file(id) NOT VALID;
 d   ALTER TABLE ONLY public.illustration_for_scope DROP CONSTRAINT illustration_for_scope_file_id_fkey;
       public          postgres    false    231    3281    219            �           2606    17482 ;   illustration_for_scope illustration_for_scope_scope_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.illustration_for_scope
    ADD CONSTRAINT illustration_for_scope_scope_id_fkey FOREIGN KEY (scope_id) REFERENCES public.scope(id) NOT VALID;
 e   ALTER TABLE ONLY public.illustration_for_scope DROP CONSTRAINT illustration_for_scope_scope_id_fkey;
       public          postgres    false    219    215    3243            �           2606    17542 >   illustration_for_version illustration_for_version_file_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.illustration_for_version
    ADD CONSTRAINT illustration_for_version_file_id_fkey FOREIGN KEY (file_id) REFERENCES public.file(id) NOT VALID;
 h   ALTER TABLE ONLY public.illustration_for_version DROP CONSTRAINT illustration_for_version_file_id_fkey;
       public          postgres    false    231    3281    233            �           2606    17547 A   illustration_for_version illustration_for_version_version_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.illustration_for_version
    ADD CONSTRAINT illustration_for_version_version_id_fkey FOREIGN KEY (version_id) REFERENCES public.version(id) NOT VALID;
 k   ALTER TABLE ONLY public.illustration_for_version DROP CONSTRAINT illustration_for_version_version_id_fkey;
       public          postgres    false    223    3267    233            �           2606    17477 N   license_agreement_file_for_scope license_agreement_file_for_scope_file_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.license_agreement_file_for_scope
    ADD CONSTRAINT license_agreement_file_for_scope_file_id_fkey FOREIGN KEY (file_id) REFERENCES public.file(id) NOT VALID;
 x   ALTER TABLE ONLY public.license_agreement_file_for_scope DROP CONSTRAINT license_agreement_file_for_scope_file_id_fkey;
       public          postgres    false    231    217    3281            �           2606    17497 0   manifest_for_IOS manifest_for_IOS_folder_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public."manifest_for_IOS"
    ADD CONSTRAINT "manifest_for_IOS_folder_id_fkey" FOREIGN KEY (folder_id) REFERENCES public.folder(id) NOT VALID;
 ^   ALTER TABLE ONLY public."manifest_for_IOS" DROP CONSTRAINT "manifest_for_IOS_folder_id_fkey";
       public          postgres    false    221    3257    222            �           2606    17552    role role_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.username(id) NOT VALID;
 @   ALTER TABLE ONLY public.role DROP CONSTRAINT role_user_id_fkey;
       public          postgres    false    227    3277    235            �           2606    17472 +   license_agreement_file_for_scope scope_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.license_agreement_file_for_scope
    ADD CONSTRAINT scope_fkey FOREIGN KEY (scope_id) REFERENCES public.scope(id) NOT VALID;
 U   ALTER TABLE ONLY public.license_agreement_file_for_scope DROP CONSTRAINT scope_fkey;
       public          postgres    false    217    215    3243            �           2606    17522 B   tag_file_for_version tag_file_for_version_file_for_version_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tag_file_for_version
    ADD CONSTRAINT tag_file_for_version_file_for_version_id_fkey FOREIGN KEY (file_for_version_id) REFERENCES public.file_for_version(id) NOT VALID;
 l   ALTER TABLE ONLY public.tag_file_for_version DROP CONSTRAINT tag_file_for_version_file_for_version_id_fkey;
       public          postgres    false    226    225    3273            �           2606    17517 5   tag_file_for_version tag_file_for_version_tag_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.tag_file_for_version
    ADD CONSTRAINT tag_file_for_version_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES public.tag(id) NOT VALID;
 _   ALTER TABLE ONLY public.tag_file_for_version DROP CONSTRAINT tag_file_for_version_tag_id_fkey;
       public          postgres    false    226    224    3269            �           2606    17532 #   user_scope user_scope_scope_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_scope
    ADD CONSTRAINT user_scope_scope_id_fkey FOREIGN KEY (scope_id) REFERENCES public.scope(id) NOT VALID;
 M   ALTER TABLE ONLY public.user_scope DROP CONSTRAINT user_scope_scope_id_fkey;
       public          postgres    false    229    215    3243            �           2606    17527 "   user_scope user_scope_user_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_scope
    ADD CONSTRAINT user_scope_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.username(id) NOT VALID;
 L   ALTER TABLE ONLY public.user_scope DROP CONSTRAINT user_scope_user_id_fkey;
       public          postgres    false    229    227    3277            �           2606    17502    version version_folder_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.version
    ADD CONSTRAINT version_folder_id_fkey FOREIGN KEY (folder_id) REFERENCES public.folder(id) NOT VALID;
 H   ALTER TABLE ONLY public.version DROP CONSTRAINT version_folder_id_fkey;
       public          postgres    false    221    3257    223            �      x������ � �      �      x������ � �      ~      x������ � �      |      x������ � �      �      x������ � �      z      x������ � �            x������ � �      �      x�3�4�v����� f      x      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �   #   x�3�,I-.�,H,.�L�B#0v����� ��"      �      x������ � �     