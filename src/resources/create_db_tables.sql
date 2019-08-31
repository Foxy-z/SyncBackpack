--
-- Structure de la table `backpacks`
--

CREATE TABLE `backpacks`
(
    `id`    int(11)     NOT NULL,
    `uuid`  varchar(36) NOT NULL,
    `level` tinyint(4)  NOT NULL DEFAULT '1'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Structure de la table `backpacks_chests`
--

CREATE TABLE `backpacks_chests`
(
    `id`     int(11)     NOT NULL,
    `server_id` varchar(36) NOT NULL,
    `name`   varchar(32) NOT NULL,
    `world`  varchar(32) NOT NULL,
    `x`      int(11)     NOT NULL,
    `y`      int(11)     NOT NULL,
    `z`      int(11)     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Structure de la table `backpacks_items`
--

CREATE TABLE `backpacks_items`
(
    `id`          int(11)      NOT NULL,
    `backpack_id` int(11)      NOT NULL,
    `slot`        tinyint(4)   NOT NULL,
    `name`        varchar(64)  NOT NULL,
    `amount`      tinyint(4)   NOT NULL DEFAULT '1',
    `texture`     varchar(512) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `backpacks`
--
ALTER TABLE `backpacks`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `uuid` (`uuid`);

--
-- Index pour la table `backpacks_chests`
--
ALTER TABLE `backpacks_chests`
    ADD PRIMARY KEY (`id`);

--
-- Index pour la table `backpacks_items`
--
ALTER TABLE `backpacks_items`
    ADD PRIMARY KEY (`id`);